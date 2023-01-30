package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

public abstract class AbstractServiceableService<E extends AbstractServiceableEntity, R extends AbstractShowableEntityRepository<E>>
        extends AbstractUsableService<E, R> {

    public AbstractServiceableService(R repository) {
        super(repository);
    }

    @Override
    public void update(Long id, E newItem) {
        E toRepo = this.show(id);
        this.serviceableToRepo(toRepo, newItem);
    }

    public void serviceableToRepo(E toRepo, E newItem) {
        toRepo.setPartNumber(newItem.getPartNumber());
        toRepo.setServiceList(newItem.getServiceList());
        toRepo.setPartList(newItem.getPartList());
        this.usableToRepo(toRepo, newItem);
    }
}
