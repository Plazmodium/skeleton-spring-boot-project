package rest.skeleton.spring.boot.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
class I18nIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validationError_shouldReturnLocalizedMessage_Default() throws Exception {
        String invalidCreate = "{\n  \"description\": \"No name\"\n}";
        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCreate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void validationError_shouldReturnLocalizedMessage_EnglishExplicit() throws Exception {
        String invalidCreate = "{\n  \"description\": \"No name\"\n}";
        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "en")
                        .content(invalidCreate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
    
    @Test
    void malformedJson_shouldReturnLocalizedMessage() throws Exception {
         String malformed = "{ name: \"Alpha\" ";
         mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformed))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is malformed or unreadable"));
    }
}
