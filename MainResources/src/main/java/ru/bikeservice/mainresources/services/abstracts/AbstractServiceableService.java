package ru.bikeservice.mainresources.services.abstracts;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.repositories.abstracts.AbstractServiceableEntityRepository;

import java.util.Optional;
import java.util.Set;

public abstract class AbstractServiceableService<E extends AbstractServiceableEntity, R extends AbstractServiceableEntityRepository<E>>
        extends AbstractUsableService<E, R>
        implements CommonServiceableEntityService<E> {

    public AbstractServiceableService(R repository) {
        super(repository);
    }

    @Override
    public E update(Long id, E newItem) {
        E toRepo = this.getById(id);
        return this.serviceableToRepo(toRepo, newItem);
    }

    public E update(E oldItem, E newItem) {
        return this.serviceableToRepo(oldItem, newItem);
    }

    E serviceableToRepo(E toRepo, E newItem) {
        toRepo.setLinkedItems(newItem.getLinkedItems());
        return this.usableToRepo(toRepo, newItem);
    }

    /**
     * Adding PartEntity to item`s E linkedItems.
     * If PartEntity is present in item`s list, type of PartEntity is checked. Consumable and Fastener increase it`s quantity, others do not affect.
     * Else new PartEntity is added.
     *
     * @param item   item E where you want to add PartEntity.
     * @param entity entity to be added.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public E addToLinkedItems(E item, PartEntity entity) {
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
        return repository.save(item);
    }

    /**
     * Deleting PartEntity from item`s E linkedItems.
     * If PartEntity is present in item`s list, type of PartEntity is checked. Consumable and Fastener decrease it`s quantity, others are deleted from list.
     * If quantity <=0 PartEntity is deleted.
     *
     * @param item   item E where you want to delete PartEntity.
     * @param entity entity to be deleted.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public E delFromLinkedItems(E item, PartEntity entity) {
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
            return repository.save(item);
        } else return item;
    }
}
