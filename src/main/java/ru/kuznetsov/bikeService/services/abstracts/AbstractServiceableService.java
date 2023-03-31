package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

import java.util.Optional;
import java.util.Set;

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
        this.usableToRepo(toRepo, newItem);
    }

    public void addToLinkedItems(E item, PartEntity entity) {
        Set<PartEntity> entitySet = item.getLinkedItems();

        Optional<PartEntity> searchItem = entitySet.stream()
                .parallel()
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

    public void delFromLinkedItems(E item, PartEntity entity) {
        Set<PartEntity> entitySet = item.getLinkedItems();
        Optional<PartEntity> searchItem = entitySet.stream()
                .parallel()
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
