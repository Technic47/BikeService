package ru.bikeservice.mainresources.repositories.abstracts;

import org.springframework.data.repository.NoRepositoryBean;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;

import java.util.List;

@NoRepositoryBean
public interface AbstractShowableEntityRepository<E extends AbstractShowableEntity>
        extends CommonRepository<E> {
    List<E> findByCreator(Long id);
    List<E> findByCreatorOrIsShared(Long id, boolean status);
    List<E> findByNameContainingIgnoreCase(String name);
    List<E> findByNameContainingIgnoreCaseAndCreatorOrIsShared(String name, Long creatorId, boolean status);
    List<E> findByDescriptionContainingIgnoreCase(String description);
    List<E> findByDescriptionContainingIgnoreCaseAndCreatorOrIsShared(String description, Long creatorId, boolean status);
    List<E> findByValueContainingIgnoreCase(String value);
    List<E> findByValueContainingIgnoreCaseAndCreatorOrIsShared(String value, Long creatorId, boolean status);
}
