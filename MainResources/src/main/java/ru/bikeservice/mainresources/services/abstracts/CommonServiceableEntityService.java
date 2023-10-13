package ru.bikeservice.mainresources.services.abstracts;

import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;
import ru.bikeservice.mainresources.models.lists.PartEntity;

public interface CommonServiceableEntityService<E extends AbstractServiceableEntity>
        extends CommonAbstractEntityService<E> {

    E addToLinkedItems(E item, PartEntity entity);

    E delFromLinkedItems(E item, PartEntity entity);
}
