package ru.kuznetsov.bikeService.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

@NoRepositoryBean
public interface CommonRepository<T extends AbstractShowableEntity> extends CrudRepository<T, Long> {
}
