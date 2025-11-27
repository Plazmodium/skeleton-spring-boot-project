package rest.skeleton.spring.boot.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest.skeleton.spring.boot.config.JwtService;

import java.util.List;
import java.util.Map;

/**
 * Simple auth controller to mint JWTs for dev/testing purposes.
 */
@RestController
@RequestMapping(path = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping(path = "/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> issueToken(@RequestBody TokenRequest req) {
        String subject = (req.subject == null || req.subject.isBlank()) ? "api-user" : req.subject;
        List<String> roles = (req.roles == null || req.roles.isEmpty()) ? List.of("ROLE_USER") : req.roles;
        String token = jwtService.issueToken(subject, roles);
        return ResponseEntity.ok(Map.of("token", token));
    }

    public static class TokenRequest {
        public String subject;
        public List<String> roles;
    }
}
