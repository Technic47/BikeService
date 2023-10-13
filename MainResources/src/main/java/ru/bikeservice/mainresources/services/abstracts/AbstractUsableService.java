package ru.bikeservice.mainresources.services.abstracts;

import ru.bikeservice.mainresources.models.abstracts.AbstractUsableEntity;
import ru.bikeservice.mainresources.repositories.abstracts.AbstractUsableEntityRepository;

import java.util.List;

public abstract class AbstractUsableService<E extends AbstractUsableEntity, R extends AbstractUsableEntityRepository<E>>
        extends AbstractShowableService<E, R>
        implements CommonUsableEntityService<E> {
    public AbstractUsableService(R repository) {
        super(repository);
    }

    @Override
    public E update(Long id, E newItem) {
        E toRepo = this.getById(id);
        return this.usableToRepo(toRepo, newItem);
    }

    @Override
    public E update(E item, E newItem) {
        return this.usableToRepo(item, newItem);
    }

    E usableToRepo(E toRepo, E newItem) {
        toRepo.setManufacturer(newItem.getManufacturer());
        toRepo.setModel(newItem.getModel());
        return this.showableToRepo(toRepo, newItem);
    }

    public List<E> findByManufacturer(Long manufacturerId) {
        return repository.findByManufacturer(manufacturerId);
    }

    public List<E> findByManufacturerAndCreatorOrIsShared(Long manufacturerId, Long userId, boolean shared) {
        return repository.findByManufacturerAndCreatorOrIsShared(manufacturerId, userId, shared);
    }

    public List<E> findByModel(String model) {
        return repository.findByModel(model);
    }

    public List<E> findByModelAndCreatorOrIsShared(String model, Long userId, boolean shared) {
        return repository.findByModelAndCreatorOrIsShared(model, userId, shared);
    }
}
