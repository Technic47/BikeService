package ru.kuznetsov.bikeService.services.abstracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * Abstract service for all Entities in project.
 *
 * @param <E>
 * @param <R>
 */
public abstract class AbstractService<E, R extends CommonRepository<E>>
        implements CommonService<E> {

    protected final R repository;
    protected ExecutorService mainExecutor;

    protected AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public E save(E entity) {
        return repository.save(entity);
    }

    @Override
    public E show(Long id) {
        Optional<E> entity = repository.findById(id);
        return entity.orElse(null);
    }

    @Override
    public void update(Long id, E updateItem) {
    }

    @Override
    public void update(E item, E updateItem) {
    }

    @Override
    public List<E> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Autowired
    @Qualifier("MainExecutor")
    public void setMainExecutor(ExecutorService mainExecutor) {
        this.mainExecutor = mainExecutor;
    }
}
