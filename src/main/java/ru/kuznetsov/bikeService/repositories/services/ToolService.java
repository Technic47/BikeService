package ru.kuznetsov.bikeService.repositories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.service.Tool;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToolService implements CommonInterface<Tool> {
    private final CommonRepository<Tool> repository;

    @Autowired
    public ToolService(CommonRepository<Tool> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Tool entity) {
        repository.save(entity);
    }

    @Override
    public Tool show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, Tool updateItem) {
        Tool toUpdate = this.show(id);
//        toUpdate.setLink(updateItem.getLink());

    }

    @Override
    public List<Tool> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
