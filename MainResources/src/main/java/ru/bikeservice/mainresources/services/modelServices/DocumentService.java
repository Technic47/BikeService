package ru.bikeservice.mainresources.services.modelServices;

import org.springframework.stereotype.Service;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.repositories.modelRepositories.DocumentRepository;
import ru.bikeservice.mainresources.services.abstracts.AbstractShowableService;

@Service
public class DocumentService extends AbstractShowableService<Document, DocumentRepository> {
    public DocumentService(DocumentRepository repository) {
        super(repository);
    }
}
