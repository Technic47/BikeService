package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

@Repository
public interface DocumentRepository extends CommonRepository<Document> {
}
