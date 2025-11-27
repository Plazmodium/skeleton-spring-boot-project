package rest.skeleton.spring.boot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import rest.skeleton.spring.boot.domain.SampleEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SampleEntityRepositoryTest {

    @Autowired
    private SampleEntityRepository repository;

    @Test
    void save_findById_and_pagination_work() {
        // save a few entities
        for (int i = 0; i < 5; i++) {
            SampleEntity e = new SampleEntity();
            e.setName("Name-" + i);
            e.setDescription("Desc-" + i);
            repository.save(e);
        }

        // find by id
        var first = repository.findAll(PageRequest.of(0, 1)).getContent().get(0);
        assertThat(first.getId()).isNotNull();
        assertThat(repository.findById(first.getId())).isPresent();

        // pagination
        Page<SampleEntity> page0 = repository.findAll(PageRequest.of(0, 2));
        Page<SampleEntity> page1 = repository.findAll(PageRequest.of(1, 2));
        assertThat(page0.getContent()).hasSize(2);
        assertThat(page1.getContent()).hasSize(2);
        assertThat(page0.getTotalElements()).isGreaterThanOrEqualTo(5);
    }
}
