package ru.kuznetsov.bikeService.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.abstracts.BaseEntity;

//@NoRepositoryBean

@Scope("prototype")
@Repository
public interface CommonRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
}
