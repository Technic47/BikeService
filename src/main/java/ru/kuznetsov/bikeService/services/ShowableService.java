package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.Optional;

@Service
@Scope("prototype")
public class ShowableService<T extends AbstractShowableEntity> implements AbstractService<T> {
    private final CommonRepository<T> repository;

    @Autowired
    public ShowableService(CommonRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public void save(T entity) {
        repository.save(entity);
    }

    @Override
    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Iterable<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
