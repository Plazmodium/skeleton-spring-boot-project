package rest.skeleton.spring.boot.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "app.security.enabled=true",
        "app.security.jwt.enabled=true",
        "app.security.jwt.acceptAnyToken=true"
})
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void get_is_permitted_while_post_requires_auth_when_security_enabled() throws Exception {
        // GET list should be permitted without auth
        mockMvc.perform(get("/api/v1/sample-entities"))
                .andExpect(status().isOk());

        // POST without Authorization should be 401
        String createJson = "{\n  \"name\": \"Secured Alpha\",\n  \"description\": \"First\"\n}";
        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isUnauthorized());

        // POST with any Bearer token should be allowed when acceptAnyToken=true
        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer test-token")
                        .content(createJson))
                .andExpect(status().isCreated());
    }
}
