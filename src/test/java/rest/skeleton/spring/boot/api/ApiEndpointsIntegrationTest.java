package rest.skeleton.spring.boot.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
class ApiEndpointsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void crudFlow_onSampleEntity() throws Exception {
        // Create
        String createJson = "{\n  \"name\": \"Alpha\",\n  \"description\": \"First\"\n}";
        var createResult = mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/sample-entities/")))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Alpha"))
                .andExpect(jsonPath("$.description").value("First"))
                .andReturn();

        // Extract created ID
        String location = createResult.getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);

        // Get by id
        mockMvc.perform(get("/api/v1/sample-entities/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Long.parseLong(id)))
                .andExpect(jsonPath("$.name").value("Alpha"));

        // List
        mockMvc.perform(get("/api/v1/sample-entities").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.content[0].id", notNullValue()));

        // PUT update
        String putJson = "{\n  \"name\": \"Alpha 2\",\n  \"description\": \"Updated\"\n}";
        mockMvc.perform(put("/api/v1/sample-entities/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(putJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alpha 2"))
                .andExpect(jsonPath("$.description").value("Updated"));

        // PATCH update
        String patchJson = "{\n  \"description\": \"Partial\"\n}";
        mockMvc.perform(patch("/api/v1/sample-entities/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Partial"));

        // Delete
        mockMvc.perform(delete("/api/v1/sample-entities/{id}", id))
                .andExpect(status().isNoContent());

        // Verify not found afterwards
        mockMvc.perform(get("/api/v1/sample-entities/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("not_found"));
    }

    @Test
    void errorResponses_validation_and_malformed_and_invalidArgument() throws Exception {
        // Validation error: missing name
        String invalidCreate = "{\n  \"description\": \"No name\"\n}";
        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCreate))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("validation_error"))
                .andExpect(jsonPath("$.details.fields.name", notNullValue()));

        // Malformed JSON
        String malformed = "{ name: \"Alpha\" "; // missing closing brace and quotes
        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformed))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("malformed_json"));

        // Create a valid entity to test invalid PUT name
        String createJson = "{\n  \"name\": \"Beta\",\n  \"description\": \"Entity\"\n}";
        var res = mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();
        String location = res.getResponse().getHeader("Location");
        String id = location.substring(location.lastIndexOf('/') + 1);

        // Invalid argument: PUT with blank name
        String invalidPut = "{\n  \"name\": \"   \",\n  \"description\": \"x\"\n}";
        mockMvc.perform(put("/api/v1/sample-entities/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPut))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("invalid_argument"));
    }
}
