package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.PartEntity;

public interface CommonServiceableEntityService<E extends AbstractServiceableEntity>
        extends CommonAbstractEntityService<E> {

    E addToLinkedItems(E item, PartEntity entity);

    E delFromLinkedItems(E item, PartEntity entity);
}
