package ru.kuznetsov.bikeService.repositories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.bike.Part;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartService implements CommonInterface<Part> {
    private final CommonRepository<Part> repository;

    @Autowired
    public PartService(CommonRepository<Part> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Part entity) {
        repository.save(entity);
    }

    @Override
    public Part show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, Part updateItem) {
        Part toUpdate = this.show(id);
//        toUpdate.setLink(updateItem.getLink());

    }

    @Override
    public List<Part> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
