package ru.kuznetsov.bikeService.repositories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumableService implements CommonInterface<Consumable> {
    private final CommonRepository<Consumable> repository;

    @Autowired
    public ConsumableService(CommonRepository<Consumable> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Consumable entity) {
        repository.save(entity);
    }

    @Override
    public Consumable show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, Consumable updateItem) {
        Consumable toUpdate = this.show(id);
//        toUpdate.setLink(updateItem.getLink());

    }

    @Override
    public List<Consumable> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
