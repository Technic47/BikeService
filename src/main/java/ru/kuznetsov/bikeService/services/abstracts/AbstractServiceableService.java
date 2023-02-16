package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

import java.util.List;

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
//        toRepo.setServiceList(newItem.getServiceList());
//        toRepo.setPartList(newItem.getPartList());
        this.usableToRepo(toRepo, newItem);
    }

    public void addToServiceList(E item, PartEntity entity){
        item.getLinkedItems().add(entity);
//        item.getLinkedItems().contains(entity);
        repository.save(item);
    }

    public void delFromServiceList(E item, PartEntity entity){
        List<PartEntity> entityList = item.getLinkedItems();
        entityList.remove(entity);
        item.setLinkedItems(entityList);
        repository.save(item);
    }
}
