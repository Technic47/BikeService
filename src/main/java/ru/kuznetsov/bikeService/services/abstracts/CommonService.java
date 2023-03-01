package ru.kuznetsov.bikeService.services.abstracts;

import java.util.List;

public interface CommonService<E> {
    E save(E entity);

    E show(Long id);

    void update(Long id, E updateItem);

    List<E> index();

    void delete(Long id);
}
