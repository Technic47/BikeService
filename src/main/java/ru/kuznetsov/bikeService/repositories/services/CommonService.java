package ru.kuznetsov.bikeService.repositories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommonService<T> implements CommonInterface<T> {
    private final CommonRepository<T> repository;

    @Autowired
    public CommonService(CommonRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public void save(T entity) {
        repository.save(entity);
    }

    @Override
    public T show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, T updateItem) {
        T toUpdate = this.show(id);
//        toUpdate.setLink(updateItem.getLink());

    }

    @Override
    public List<T> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
