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
    public void update(Long id, E newItem) {
        E torepo = this.show(id);
        this.showableToRepo(torepo, newItem);
    }

    public void showableToRepo(E toRepo, E newItem) {
        toRepo.setName(newItem.getName());
        toRepo.setDescription(newItem.getDescription());
        toRepo.setPicture(newItem.getPicture());
        toRepo.setLink(newItem.getLink());
        toRepo.setValue(newItem.getValue());
        this.save(toRepo);
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
