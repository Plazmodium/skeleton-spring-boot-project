package rest.skeleton.spring.boot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rest.skeleton.spring.boot.domain.SampleEntity;
import rest.skeleton.spring.boot.repository.SampleEntityRepository;

import java.util.Optional;

@Service
public class SampleEntityService {
    private final SampleEntityRepository repository;

    public SampleEntityService(SampleEntityRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public SampleEntity create(SampleEntity entity) {
        entity.setId(null);
        return repository.save(entity);
    }

    @Transactional(readOnly = true)
    public SampleEntity getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SampleEntity not found: id=" + id));
    }

    @Transactional(readOnly = true)
    public Page<SampleEntity> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public SampleEntity updatePut(Long id, String name, String description) {
        SampleEntity existing = getById(id);
        existing.setName(name);
        existing.setDescription(description);
        return repository.save(existing);
    }

    @Transactional
    public SampleEntity updatePatch(Long id, Optional<String> name, Optional<String> description) {
        SampleEntity existing = getById(id);
        name.ifPresent(existing::setName);
        description.ifPresent(existing::setDescription);
        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("SampleEntity not found: id=" + id);
        }
        repository.deleteById(id);
    }
}
