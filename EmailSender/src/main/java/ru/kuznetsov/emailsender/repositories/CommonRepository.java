package ru.kuznetsov.emailsender.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonRepository<E> extends JpaRepository<E, Long> {
}
