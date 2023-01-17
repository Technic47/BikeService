package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;

@Repository
public interface AbstractServiceableEntityRepository extends CommonRepository<AbstractServiceableEntity> {
}
