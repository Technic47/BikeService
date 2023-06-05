package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

import java.util.List;

public interface CommonAbstractEntityService<E extends AbstractShowableEntity>
        extends CommonService<E> {
    List<E> findByCreator(Long id);

    /**
     * Find records that have either creator id match or are shared.
     * @param id creators id
     * @return list of matching records.
     */
    List<E> findByCreatorOrShared(Long id);

    /**
     * Find records containing argument String in Name field.
     * @param string search value
     * @return list of matching records.
     */
    List<E> findByNameContainingIgnoreCase(String string);

    /**
     * Find records containing argument String in Description field.
     * @param string search value
     * @return list of matching records.
     */
    List<E> findByDescriptionContainingIgnoreCase(String string);
}
