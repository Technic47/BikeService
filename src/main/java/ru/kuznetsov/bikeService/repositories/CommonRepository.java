package ru.kuznetsov.bikeService.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

//@NoRepositoryBean

@Scope("prototype")
@Repository
public interface CommonRepository<T extends AbstractShowableEntity> extends CrudRepository<T, Long> {
}
