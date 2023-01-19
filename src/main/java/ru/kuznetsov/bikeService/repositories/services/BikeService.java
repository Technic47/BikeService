package ru.kuznetsov.bikeService.repositories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.bike.Bike;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BikeService implements CommonInterface<Bike> {
    private final CommonRepository<Bike> repository;

    @Autowired
    public BikeService(CommonRepository<Bike> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Bike entity) {
        repository.save(entity);
    }

    @Override
    public Bike show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, Bike updateItem) {
        Bike toUpdate = this.show(id);
//        toUpdate.setLink(updateItem.getLink());

    }

    @Override
    public List<Bike> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
