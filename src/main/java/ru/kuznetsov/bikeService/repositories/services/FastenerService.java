package ru.kuznetsov.bikeService.repositories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.service.Fastener;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class FastenerService implements CommonInterface<Fastener> {
    private final CommonRepository<Fastener> repository;

    @Autowired
    public FastenerService(CommonRepository<Fastener> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Fastener entity) {
        repository.save(entity);
    }

    @Override
    public Fastener show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, Fastener updateItem) {
        Fastener toUpdate = this.show(id);
//        toUpdate.setLink(updateItem.getLink());

    }

    @Override
    public List<Fastener> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
