package ru.kuznetsov.bikeService.repositories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class PictureRepository implements CommonInterface<Picture>{
    private final CommonRepository<Picture> repository;

    @Autowired
    public PictureRepository(CommonRepository<Picture> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Picture entity) {
        repository.save(entity);
    }

    @Override
    public Picture show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, Picture updateItem) {
        Picture toUpdate = this.show(id);
//        toUpdate.setLink(updateItem.getLink());

    }

    @Override
    public List<Picture> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
