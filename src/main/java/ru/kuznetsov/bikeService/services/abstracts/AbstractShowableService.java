package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;


public abstract class AbstractShowableService<E extends AbstractShowableEntity, R extends CommonRepository<E>> extends AbstractService<E, R>
        implements CommonService<E> {


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
}
