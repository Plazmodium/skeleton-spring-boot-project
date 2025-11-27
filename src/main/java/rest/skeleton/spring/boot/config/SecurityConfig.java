package rest.skeleton.spring.boot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig {

    private final SecurityProperties props;

    public SecurityConfig(SecurityProperties props) {
        this.props = props;
    }

    @Bean
    public JwtService jwtService() {
        return new JwtService(props);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Common security setup
        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // allow H2 console frames
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        if (props.isEnabled()) {
            // Authorization when security is enabled
            http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/health",
                    "/h2-console/**",
                    "/api/v1/auth/token"
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
            );

            if (props.getJwt().isEnabled()) {
                if (props.getJwt().isAcceptAnyToken()) {
                    http.addFilterBefore(new StubJwtFilter(props), UsernamePasswordAuthenticationFilter.class);
                } else {
                    http.addFilterBefore(new JwtAuthenticationFilter(jwtService()), UsernamePasswordAuthenticationFilter.class);
                }
            }
        } else {
            // Security disabled: permit everything
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        }

        // Basic exception handling defaults are fine (401 for unauthorized)
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Very small stub filter to simulate JWT auth when enabled. If acceptAnyToken is true and a Bearer token
     * is present, it authenticates the request with a generic user principal.
     */
    static class StubJwtFilter extends OncePerRequestFilter {
        private final SecurityProperties properties;

        StubJwtFilter(SecurityProperties properties) {
            this.properties = properties;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            if (properties.getJwt().isEnabled() && properties.getJwt().isAcceptAnyToken()) {
                String auth = request.getHeader("Authorization");
                if (auth != null && auth.startsWith("Bearer ")) {
                    String token = auth.substring(7);
                    if (!token.isBlank()) {
                        var authToken = new UsernamePasswordAuthenticationToken(
                                "api-user", token, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    /**
     * JWT authentication filter that validates HS256 tokens and sets SecurityContext when valid.
     */
    static class JwtAuthenticationFilter extends OncePerRequestFilter {
        private final JwtService jwtService;

        JwtAuthenticationFilter(JwtService jwtService) {
            this.jwtService = jwtService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String auth = request.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                String token = auth.substring(7);
                try {
                    var claims = jwtService.validateAndParse(token);
                    String subject = claims.getSubject();
                    var roles = (List<?>) claims.getOrDefault("roles", List.of());
                    var authorities = roles.stream()
                            .map(String::valueOf)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    var authentication = new UsernamePasswordAuthenticationToken(subject, token, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception e) {
                    // Invalid token: leave context unauthenticated; downstream will return 401 if required
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
