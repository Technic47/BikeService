package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.repositories.DocumentRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class DocumentService extends AbstractService<Document, DocumentRepository> {

    public DocumentService(DocumentRepository repository) {
        super(repository);
    }
}
