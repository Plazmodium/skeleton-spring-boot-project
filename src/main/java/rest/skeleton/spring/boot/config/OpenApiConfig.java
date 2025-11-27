package rest.skeleton.spring.boot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SpringRestSkeleton API",
                version = "1.0",
                description = "Swagger/OpenAPI documentation for the Spring REST Skeleton project."
        ),
        servers = {
                @Server(url = "/", description = "Default Server")
        }
)
public class OpenApiConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info()
            .title("SpringRestSkeleton API")
            .version("1.0")
            .description("Interactive API documentation for SampleEntity endpoints and error models.")
            .contact(new Contact().name("API Maintainers").email("devnull@example.com"))
            .license(new License().name("MIT"))
        );
    }
}
