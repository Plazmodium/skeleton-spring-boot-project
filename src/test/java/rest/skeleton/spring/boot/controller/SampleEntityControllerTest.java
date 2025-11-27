package rest.skeleton.spring.boot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import rest.skeleton.spring.boot.domain.SampleEntity;
import rest.skeleton.spring.boot.service.ResourceNotFoundException;
import rest.skeleton.spring.boot.service.SampleEntityService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SampleEntityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class SampleEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SampleEntityService service;

    @Test
    void create_validPayload_returnsCreated() throws Exception {
        SampleEntity created = new SampleEntity();
        created.setId(1L);
        created.setName("Test");
        created.setDescription("Desc");

        when(service.create(any(SampleEntity.class))).thenReturn(created);

        String payload = """
                {
                    "name": "Test",
                    "description": "Desc"
                }
                """;

        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void create_invalidPayload_returnsBadRequest() throws Exception {
        String payload = """
                {
                    "description": "No name"
                }
                """;

        mockMvc.perform(post("/api/v1/sample-entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("validation_error"));
    }

    @Test
    void getById_exists_returnsOk() throws Exception {
        SampleEntity entity = new SampleEntity();
        entity.setId(1L);
        entity.setName("Test");

        when(service.getById(1L)).thenReturn(entity);

        mockMvc.perform(get("/api/v1/sample-entities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getById_notExists_returnsNotFound() throws Exception {
        when(service.getById(99L)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/v1/sample-entities/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("not_found"));
    }

    @Test
    void list_returnsOk() throws Exception {
        Page<SampleEntity> page = new PageImpl<>(Collections.emptyList());
        when(service.list(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/sample-entities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void updatePut_valid_returnsOk() throws Exception {
        SampleEntity updated = new SampleEntity();
        updated.setId(1L);
        updated.setName("Updated");
        updated.setDescription("Desc");

        when(service.updatePut(eq(1L), eq("Updated"), eq("Desc"))).thenReturn(updated);

        String payload = """
                {
                    "name": "Updated",
                    "description": "Desc"
                }
                """;

        mockMvc.perform(put("/api/v1/sample-entities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void updatePut_invalid_returnsBadRequest() throws Exception {
        String payload = """
                {
                    "name": "",
                    "description": "Desc"
                }
                """;

        mockMvc.perform(put("/api/v1/sample-entities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_exists_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/sample-entities/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void delete_notExists_returnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Not found")).when(service).delete(99L);
        
        mockMvc.perform(delete("/api/v1/sample-entities/99"))
                .andExpect(status().isNotFound());
    }
}
