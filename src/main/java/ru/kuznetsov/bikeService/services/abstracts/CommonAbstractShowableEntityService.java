package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

import java.util.List;

public interface CommonAbstractShowableEntityService<E extends AbstractShowableEntity>
        extends CommonService<E> {
    public List<E> findByCreator(Long id);
}
