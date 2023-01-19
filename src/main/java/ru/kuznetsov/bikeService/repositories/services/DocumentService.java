package ru.kuznetsov.bikeService.repositories.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class DocumentService implements CommonInterface<Document> {
    private final CommonRepository<Document> repository;
    @Autowired
    public DocumentService(CommonRepository<Document> repository) {
        this.repository = repository;
    }

    @Override
    public void save(Document entity) {
        repository.save(entity);
    }

    @Override
    public Document show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, Document updateItem) {
        Document toUpdate = this.show(id);
//        toUpdate.setLink(updateItem.getLink());

    }

    @Override
    public List<Document> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
