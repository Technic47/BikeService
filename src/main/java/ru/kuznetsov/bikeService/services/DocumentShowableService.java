package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.repositories.DocumentRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractShowableService;

@Service
public class DocumentShowableService extends AbstractShowableService<Document, DocumentRepository> {

    public DocumentShowableService(DocumentRepository repository) {
        super(repository);
    }
}
