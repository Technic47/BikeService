package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

import java.util.List;

/**
 * Abstract service for main categories of project.
 * Showable, Usable and Serviceable.
 * @param <E>
 * @param <R>
 */
public abstract class AbstractShowableService<E extends AbstractShowableEntity,
        R extends AbstractShowableEntityRepository<E>> extends AbstractService<E, R>
        implements CommonAbstractEntityService<E> {

    public AbstractShowableService(R repository) {
        super(repository);
    }

    /**
     * Method for updating item with id.
     * Parameters are taken from E newItem.
     *
     * @param id      id of item that will be updated.
     * @param newItem donor form fields.
     */
    @Override
    public void update(Long id, E newItem) {
        E toRepo = this.show(id);
        this.showableToRepo(toRepo, newItem);
    }

    public void update(E item, E newItem){
        this.showableToRepo(item, newItem);
    }

    void showableToRepo(E toRepo, E newItem) {
        toRepo.setName(newItem.getName());
        toRepo.setDescription(newItem.getDescription());
        toRepo.setPicture(newItem.getPicture());
        toRepo.setLink(newItem.getLink());
        toRepo.setValue(newItem.getValue());
        toRepo.setIsShared(newItem.getIsShared());
        this.save(toRepo);
    }

    /**
     * Find items E with specified creator.
     *
     * @param id Long id of creator.
     * @return List of items E.
     */
    public List<E> findByCreator(Long id) {
        return repository.findByCreator(id);
    }

    /**
     * Find records that have either creator id match or are shared.
     *
     * @param id creators id
     * @return list of matching records.
     */
    public List<E> findByCreatorOrShared(Long id) {
        return this.repository.findByCreatorOrIsShared(id, true);
    }

    /**
     * Find records containing argument String in Name field.
     *
     * @param string search value
     * @return list of matching records.
     */
    public List<E> findByNameContainingIgnoreCase(String string) {
        return this.repository.findByNameContainingIgnoreCase(string);
    }

    /**
     * Find records containing argument String in Description field.
     *
     * @param string search value
     * @return list of matching records.
     */
    public List<E> findByDescriptionContainingIgnoreCase(String string) {
        return this.repository.findByDescriptionContainingIgnoreCase(string);
    }
}
