package ru.kuznetsov.bikeService.repositories.abstracts;

import org.springframework.data.repository.NoRepositoryBean;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

import java.util.List;

@NoRepositoryBean
public interface AbstractShowableEntityRepository<E extends AbstractShowableEntity>
        extends CommonRepository<E> {
    List<E> findByCreator(Long id);
    List<E> findByCreatorOrIsShared(Long id, boolean status);
    List<E> findByNameContainingIgnoreCase(String name);
    List<E> findByDescriptionContainingIgnoreCase(String description);
}
