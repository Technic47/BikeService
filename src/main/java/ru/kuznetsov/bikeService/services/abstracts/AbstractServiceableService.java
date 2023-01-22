package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractServiceableService<E extends AbstractServiceableEntity, R extends CommonRepository<E>>
        implements CommonService<E> {

    private final R repository;

    public AbstractServiceableService(R repository) {
        this.repository = repository;
    }

    @Override
    public void save(E entity) {
        repository.save(entity);
    }

    @Override
    public E show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, E updateItem) {
        E toUpdate = this.show(id);

    }

    @Override
    public List<E> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
