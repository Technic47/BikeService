package ru.bikeservice.mainresources.services.abstracts;

import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;

import java.util.List;

public interface CommonAbstractEntityService<E extends AbstractShowableEntity>
        extends CommonService<E> {
    List<E> findByCreator(Long id);

    List<E> findByCreatorOrShared(Long id);

    List<E> findByNameContainingIgnoreCase(String string);

    List<E> findByNameContainingIgnoreCaseAndCreatorOrIsShared(String string, Long creatorId, boolean shared);

    List<E> findByDescriptionContainingIgnoreCase(String string);

    List<E> findByValueContainingIgnoreCase(String value);
}
