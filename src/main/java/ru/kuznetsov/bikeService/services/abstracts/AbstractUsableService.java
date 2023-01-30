package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

public abstract class AbstractUsableService<E extends AbstractUsableEntity, R extends AbstractShowableEntityRepository<E>>
        extends AbstractShowableService<E, R> {
    public AbstractUsableService(R repository) {
        super(repository);
    }

    @Override
    public void update(Long id, E newItem) {
        E toRepo = this.show(id);
        this.usableToRepo(toRepo, newItem);
    }

    public void usableToRepo(E toRepo, E newItem) {
        toRepo.setManufacturer(newItem.getManufacturer());
        toRepo.setModel(newItem.getModel());
        this.showableToRepo(toRepo, newItem);
    }
}
