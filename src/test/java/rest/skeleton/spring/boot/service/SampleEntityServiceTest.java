package rest.skeleton.spring.boot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rest.skeleton.spring.boot.domain.SampleEntity;
import rest.skeleton.spring.boot.repository.SampleEntityRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SampleEntityServiceTest {

    @Mock
    private SampleEntityRepository repository;

    @InjectMocks
    private SampleEntityService service;

    @Test
    void create_shouldSaveEntity() {
        SampleEntity input = new SampleEntity();
        input.setName("Test");
        
        SampleEntity saved = new SampleEntity();
        saved.setId(1L);
        saved.setName("Test");

        when(repository.save(any(SampleEntity.class))).thenReturn(saved);

        SampleEntity result = service.create(input);

        assertNotNull(result.getId());
        assertEquals("Test", result.getName());
        verify(repository).save(input);
    }

    @Test
    void getById_whenExists_shouldReturnEntity() {
        SampleEntity existing = new SampleEntity();
        existing.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        SampleEntity result = service.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getById_whenNotExists_shouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(99L));
    }

    @Test
    void list_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<SampleEntity> page = new PageImpl<>(Collections.emptyList());
        when(repository.findAll(pageable)).thenReturn(page);

        Page<SampleEntity> result = service.list(pageable);

        assertEquals(0, result.getTotalElements());
    }

    @Test
    void updatePut_shouldUpdateAllFields() {
        SampleEntity existing = new SampleEntity();
        existing.setId(1L);
        existing.setName("Old");
        existing.setDescription("Old Desc");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(SampleEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SampleEntity result = service.updatePut(1L, "New", "New Desc");

        assertEquals("New", result.getName());
        assertEquals("New Desc", result.getDescription());
    }

    @Test
    void updatePatch_shouldUpdateOnlyProvidedFields() {
        SampleEntity existing = new SampleEntity();
        existing.setId(1L);
        existing.setName("Old");
        existing.setDescription("Old Desc");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(SampleEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SampleEntity result = service.updatePatch(1L, Optional.of("New"), Optional.empty());

        assertEquals("New", result.getName());
        assertEquals("Old Desc", result.getDescription());
    }

    @Test
    void delete_whenExists_shouldDelete() {
        when(repository.existsById(1L)).thenReturn(true);
        
        service.delete(1L);
        
        verify(repository).deleteById(1L);
    }

    @Test
    void delete_whenNotExists_shouldThrowException() {
        when(repository.existsById(99L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));
    }
}
