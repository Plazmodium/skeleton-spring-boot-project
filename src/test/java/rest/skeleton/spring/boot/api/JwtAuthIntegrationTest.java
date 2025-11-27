package rest.skeleton.spring.boot.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "app.security.enabled=true",
        "app.security.jwt.enabled=true",
        "app.security.jwt.acceptAnyToken=false"
})
class JwtAuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void tokenIssued_allowsSecuredPost_whenProvided() throws Exception {
        // Without token: 401
        String createJson = "{\n  \"name\": \"Secure Alpha\",\n  \"description\": \"With JWT\"\n}";
        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isUnauthorized());

        // Obtain token
        var tokenResult = mockMvc.perform(post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n  \"subject\": \"tester\",\n  \"roles\": [\"ROLE_USER\"]\n}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andReturn();

        String body = tokenResult.getResponse().getContentAsString();
        com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
        java.util.Map<?,?> map = om.readValue(body, java.util.Map.class);
        String token = String.valueOf(map.get("token"));

        // Use token
        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(createJson))
                .andExpect(status().isCreated());
    }
}
