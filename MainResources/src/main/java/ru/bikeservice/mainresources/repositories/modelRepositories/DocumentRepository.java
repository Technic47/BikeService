package ru.bikeservice.mainresources.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.repositories.abstracts.AbstractShowableEntityRepository;

@Repository
public interface DocumentRepository extends AbstractShowableEntityRepository<Document> {
}
