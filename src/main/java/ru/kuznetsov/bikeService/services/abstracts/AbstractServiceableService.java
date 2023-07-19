package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

import java.util.Optional;
import java.util.Set;

public abstract class AbstractServiceableService<E extends AbstractServiceableEntity,
        R extends AbstractShowableEntityRepository<E>>
        extends AbstractUsableService<E, R> implements CommonServiceableEntityService<E> {

    public AbstractServiceableService(R repository) {
        super(repository);
    }

    @Override
    public void update(Long id, E newItem) {
        E toRepo = this.show(id);
        this.serviceableToRepo(toRepo, newItem);
    }

    public void update(E oldItem, E newItem){
        this.serviceableToRepo(oldItem, newItem);
    }

    void serviceableToRepo(E toRepo, E newItem) {
        this.usableToRepo(toRepo, newItem);
    }

    /**
     * Adding PartEntity to item`s E linkedItems.
     * If PartEntity is present in item`s list, type of PartEntity is checked. Consumable and Fastener increase it`s quantity, others do not affect.
     * Else new PartEntity is added.
     *
     * @param item   item E where you want to add PartEntity.
     * @param entity entity to be added.
     */
    public void addToLinkedItems(E item, PartEntity entity) {
        Set<PartEntity> entitySet = item.getLinkedItems();

        Optional<PartEntity> searchItem = entitySet.stream()
                .filter(part -> part.equals(entity))
                .findFirst();

        if (searchItem.isPresent()) {
            switch (searchItem.get().getType()) {
                case "Consumable", "Fastener":
                    PartEntity changeAmount = searchItem.get();
                    entitySet.remove(changeAmount);
                    changeAmount.setAmount(changeAmount.getAmount() + entity.getAmount());
                    entitySet.add(changeAmount);
                    break;
                case "Part", "Document", "Tool":
                    break;
            }
        } else {
            entitySet.add(entity);
        }
        repository.save(item);
    }

    /**
     * Deleting PartEntity from item`s E linkedItems.
     * If PartEntity is present in item`s list, type of PartEntity is checked. Consumable and Fastener decrease it`s quantity, others are deleted from list.
     * If quantity <=0 PartEntity is deleted.
     *
     * @param item   item E where you want to delete PartEntity.
     * @param entity entity to be deleted.
     */
    public void delFromLinkedItems(E item, PartEntity entity) {
        Set<PartEntity> entitySet = item.getLinkedItems();
        Optional<PartEntity> searchItem = entitySet.stream()
                .filter(part -> part.equals(entity))
                .findFirst();

        if (searchItem.isPresent()) {
            switch (searchItem.get().getType()) {
                case "Consumable", "Fastener" -> {
                    PartEntity changeAmount = searchItem.get();
                    if ((changeAmount.getAmount() - entity.getAmount()) <= 0) {
                        entitySet.remove(changeAmount);
                    } else {
                        entitySet.remove(changeAmount);
                        changeAmount.setAmount(changeAmount.getAmount() - entity.getAmount());
                        entitySet.add(changeAmount);
                    }
                }
                case "Part", "Document", "Tool" -> entitySet.remove(entity);
            }
            repository.save(item);
        }
    }
}
