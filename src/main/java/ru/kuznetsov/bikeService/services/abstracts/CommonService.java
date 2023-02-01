package ru.kuznetsov.bikeService.services.abstracts;

import java.util.List;

public interface CommonService<E> {
    public E save(E entity);

    public E show(Long id);

    public void update(Long id, E updateItem);

    public List<E> index();

    public void delete(Long id);
}
