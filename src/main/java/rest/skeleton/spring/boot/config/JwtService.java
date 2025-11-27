package rest.skeleton.spring.boot.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Minimal JWT service for issuing and validating HS256 tokens.
 */
public class JwtService {
    private final SecurityProperties properties;
    private final SecretKey secretKey;

    public JwtService(SecurityProperties properties) {
        this.properties = properties;
        byte[] keyBytes = Decoders.BASE64.decode(properties.getJwt().getSecret());
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String issueToken(String subject, List<String> roles) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(properties.getJwt().getExpirationSeconds());
        return Jwts.builder()
                .subject(subject)
                .issuer(properties.getJwt().getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claims(Map.of("roles", roles))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateAndParse(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(properties.getJwt().getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
