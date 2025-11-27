package rest.skeleton.spring.boot.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
class OpenApiSpecIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void swaggerConfig_isServed_andPointsToApiDocs() throws Exception {
        mockMvc.perform(get("/v3/api-docs/swagger-config").accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                // Either `url` or `urls` may be present depending on config; assert presence of base docs URL string
                .andExpect(content().string(containsString("/v3/api-docs")));
    }
}
