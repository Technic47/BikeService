package ru.kuznetsov.bikeService.services.abstracts;

import java.util.List;

public interface CommonService<E> {
    E save(E entity);

    E getById(Long id);

    void update(Long id, E updateItem);

    void update(E item, E updateItem);

    List<E> index();

    void delete(Long id);
}
