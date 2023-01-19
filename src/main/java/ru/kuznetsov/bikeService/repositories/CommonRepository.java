package ru.kuznetsov.bikeService.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Scope("prototype")
@Repository
//@NoRepositoryBean
public interface CommonRepository<T> extends JpaRepository<T, Long> {
}
