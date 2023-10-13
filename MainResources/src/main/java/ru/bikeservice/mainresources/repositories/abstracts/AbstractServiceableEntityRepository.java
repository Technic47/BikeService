package ru.bikeservice.mainresources.repositories.abstracts;

import org.springframework.data.repository.NoRepositoryBean;
import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;

@NoRepositoryBean
public interface AbstractServiceableEntityRepository<E extends AbstractServiceableEntity>
        extends AbstractUsableEntityRepository<E> {
}
