package ru.kuznetsov.bikeService.services.abstracts;

import org.springframework.scheduling.annotation.Async;
import ru.kuznetsov.bikeService.customExceptions.ResourceNotFoundException;

import java.util.List;

public interface CommonService<E> {
    E save(E entity);

    E getById(Long id) throws ResourceNotFoundException;

    E update(Long id, E updateItem);

    E update(E item, E updateItem);

    @Async
    List<E> index();

    void delete(Long id) throws ResourceNotFoundException;
}
