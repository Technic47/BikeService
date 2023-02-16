package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.PartEntity;

public interface CommonServiceableEntityService<E extends AbstractServiceableEntity>
        extends CommonAbstractEntityService<E> {

    void addToLinkedItems(E item, PartEntity entity);

    void delFromLinkedItems(E item, PartEntity entity);
}
