package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractShowableService<E extends AbstractShowableEntity, R extends CommonRepository<E>>
        implements CommonService<E> {
    private final R repository;

    public AbstractShowableService(R repository) {
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
        toUpdate.setName(updateItem.getName());
        toUpdate.setDescription(updateItem.getDescription());
        toUpdate.setPicture(updateItem.getPicture());
        toUpdate.setLink(updateItem.getLink());
        toUpdate.setValue(updateItem.getValue());
        this.save(toUpdate);
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
