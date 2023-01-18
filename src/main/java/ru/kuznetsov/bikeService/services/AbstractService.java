package ru.kuznetsov.bikeService.services;

import java.util.Optional;


public interface AbstractService<T> {
    void save(T entity);

    Optional<T> findById(Long id);

    Iterable<T> findAll();

    void deleteById(Long id);
}
