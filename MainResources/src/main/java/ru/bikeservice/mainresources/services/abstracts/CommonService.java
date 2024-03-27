package ru.bikeservice.mainresources.services.abstracts;

import ru.bikeservice.mainresources.customExceptions.ResourceNotFoundException;

import java.util.List;

public interface CommonService<E> {
    E save(E entity);

    E getById(Long id) throws ResourceNotFoundException;

    E update(Long id, E updateItem);

    E update(E item, E updateItem);

    List<E> getAll();

    void delete(Long id) throws ResourceNotFoundException;
}
