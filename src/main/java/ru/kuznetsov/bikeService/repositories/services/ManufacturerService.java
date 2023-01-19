package ru.kuznetsov.bikeService.repositories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class ManufacturerService implements CommonInterface<Manufacturer> {
    private final CommonRepository<Manufacturer> repository;

    @Autowired
    public ManufacturerService(CommonRepository<Manufacturer> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Manufacturer entity) {
        repository.save(entity);
    }

    @Override
    public Manufacturer show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, Manufacturer updateItem) {
        Manufacturer toUpdate = this.show(id);
//        toUpdate.setLink(updateItem.getLink());

    }

    @Override
    public List<Manufacturer> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
