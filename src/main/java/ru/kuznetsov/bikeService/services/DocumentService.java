package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.repositories.DocumentRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractShowableService;

@Service
public class DocumentService extends AbstractShowableService<Document, DocumentRepository> {

    public DocumentService(DocumentRepository repository) {
        super(repository);
    }
}
