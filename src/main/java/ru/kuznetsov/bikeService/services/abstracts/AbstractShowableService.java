package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

import java.util.List;


public abstract class AbstractShowableService<E extends AbstractShowableEntity, R extends AbstractShowableEntityRepository<E>> extends AbstractService<E, R>
        implements CommonAbstractShowableEntityService<E> {

    public AbstractShowableService(R repository) {
        super(repository);
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

    public List<E> findByCreator(Long id) {
        return repository.findByCreator(id);
    }
}
