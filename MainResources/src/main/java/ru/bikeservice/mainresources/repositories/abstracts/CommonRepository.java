package ru.bikeservice.mainresources.repositories.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonRepository<E> extends JpaRepository<E, Long> {
}
