package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

public abstract class AbstractUsableService<E extends AbstractUsableEntity,
        R extends AbstractShowableEntityRepository<E>>
        extends AbstractShowableService<E, R> {
    public AbstractUsableService(R repository) {
        super(repository);
    }

    @Override
    public E update(Long id, E newItem) {
        E toRepo = this.getById(id);
        return this.usableToRepo(toRepo, newItem);
    }

    E usableToRepo(E toRepo, E newItem) {
        toRepo.setManufacturer(newItem.getManufacturer());
        toRepo.setModel(newItem.getModel());
        return this.showableToRepo(toRepo, newItem);
    }
}
