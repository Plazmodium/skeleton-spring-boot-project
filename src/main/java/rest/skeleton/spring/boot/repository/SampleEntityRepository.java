package rest.skeleton.spring.boot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rest.skeleton.spring.boot.domain.SampleEntity;

public interface SampleEntityRepository extends JpaRepository<SampleEntity, Long> {
    Page<SampleEntity> findAll(Pageable pageable);
}
