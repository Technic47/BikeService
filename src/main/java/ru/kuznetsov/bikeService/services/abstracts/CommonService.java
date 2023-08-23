package ru.kuznetsov.bikeService.services.abstracts;

import java.util.List;

public interface CommonService<E> {
    E save(E entity);

    E getById(Long id);

    E update(Long id, E updateItem);

    E update(E item, E updateItem);

    List<E> index();

    void delete(Long id);
}
