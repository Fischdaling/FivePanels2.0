package theShire.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.theShire.domain.BaseEntity;
import org.theShire.repository.AbstractRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AbstractRepositoryTest<T extends BaseEntity> {

    private AbstractRepository<T> repository;
    T entity1;
    T entity2;
    @BeforeEach
    void setUp() {
        repository = new AbstractRepository<T>() {
            @Override
            public void saveEntryMap(String filepath) {
            }

            @Override
            public void loadEntryMap(String filepath) {
            }
        };
        entity1 = (T)new BaseEntity();
        entity2 = (T)new BaseEntity();
    }

    @Test
    void save_ShouldSaveEntityInRepo_WhenEntityExists() {
        repository.save(entity1);
        assertTrue(repository.existsById(entity1.getEntityId()));
    }


    @Test
    void findAll_ShouldReturnAllEntities_WhenEntityExists() {
        
        repository.save(entity1);
        repository.save(entity2);
        List<T> entities = repository.findAll();
        assertEquals(2, entities.size());
        assertTrue(entities.contains(entity1));
        assertTrue(entities.contains(entity2));
    }


    @Test
    void findByID_ShouldReturnEntity_WhenEntityExists() {
        repository.save(entity1);
        assertEquals(entity1, repository.findByID(entity1.getEntityId()));
    }

    @Test
    void deleteById_ShouldDeleteEntity_WhenEntityExists() {
        repository.save(entity1);
        assertTrue(repository.existsById(entity1.getEntityId()));
        repository.deleteById(entity1.getEntityId());
        assertFalse(repository.existsById(entity1.getEntityId()));
    }

    @Test
    void count_ShouldReturnNumberOfBaseentitys_WhenEntityExists() {
        assertEquals(0, repository.count());
        repository.save(entity1);
        assertEquals(1, repository.count());
    }

    @Test
    void existsById_ShouldReturnFalse_WhenEntityDoesNotExist() {
        UUID id = UUID.randomUUID();

        assertFalse(repository.existsById(id));
    }

    @Test
    void existsById_ShouldReturnTrue_WhenEntityExist() {
        assertFalse(repository.existsById(entity1.getEntityId()));
        repository.save((entity1));
        assertTrue(repository.existsById(entity1.getEntityId()));
    }

    @Test
    void deleteAll_ShouldDeleteAllEntities_WhenEntityExists() {
        repository.save(entity1);
        repository.save(entity2);
        assertEquals(2, repository.count());
        repository.deleteAll();
        assertEquals(0, repository.count());
    }

    @Test
    void saveAll_ShouldSaveAllEntities_WhenEntityExists() {
        List<T> entities = new ArrayList<>();
        assertEquals(0, repository.count());
        entities.add(entity1);
        entities.add(entity2);
        repository.saveAll(entities);
        assertEquals(2, repository.count());
    }
}
