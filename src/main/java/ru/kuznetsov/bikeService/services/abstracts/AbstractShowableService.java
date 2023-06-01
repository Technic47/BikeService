package ru.kuznetsov.bikeService.services.abstracts;

import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

import java.util.List;


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

    void showableToRepo(E toRepo, E newItem) {
        toRepo.setName(newItem.getName());
        toRepo.setDescription(newItem.getDescription());
        toRepo.setPicture(newItem.getPicture());
        toRepo.setLink(newItem.getLink());
        toRepo.setValue(newItem.getValue());
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

    public List<E> findByNameContainingIgnoreCase(String string) {
        return this.repository.findByNameContainingIgnoreCase(string);
    }

    public List<E> findByDescriptionContainingIgnoreCase(String string){
        return this.repository.findByDescriptionContainingIgnoreCase(string);
    }
}
