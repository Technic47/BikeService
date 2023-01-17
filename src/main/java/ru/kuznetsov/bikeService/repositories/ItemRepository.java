package ru.kuznetsov.bikeService.repositories;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepository<T> extends CrudRepository<T, Long> {
}
