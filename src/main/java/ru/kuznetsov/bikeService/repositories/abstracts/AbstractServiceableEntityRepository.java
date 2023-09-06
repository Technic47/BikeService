package ru.kuznetsov.bikeService.repositories.abstracts;

import org.springframework.data.repository.NoRepositoryBean;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;

@NoRepositoryBean
public interface AbstractServiceableEntityRepository<E extends AbstractServiceableEntity>
        extends AbstractUsableEntityRepository<E> {
}
