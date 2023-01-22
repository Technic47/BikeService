package ru.kuznetsov.bikeService.repositories.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

//@Scope("prototype")
//@Repository
@NoRepositoryBean
public interface CommonRepository<E> extends JpaRepository<E, Long> {
}
