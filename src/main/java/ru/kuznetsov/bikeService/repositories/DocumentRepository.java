package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

@Repository
public interface DocumentRepository extends AbstractShowableEntityRepository<Document> {
}
