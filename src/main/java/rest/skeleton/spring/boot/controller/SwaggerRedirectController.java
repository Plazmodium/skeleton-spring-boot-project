package rest.skeleton.spring.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Provides stable redirect endpoints for Swagger UI under conventional paths.
 * Ensures both /swagger-ui.html and /api/v1/swagger-ui.html are accessible.
 */
@Controller
public class SwaggerRedirectController {

    @GetMapping({"/api/v1/swagger-ui.html"})
    public String redirectApiSwaggerUi() {
        return "redirect:/swagger-ui/index.html";
    }
}
