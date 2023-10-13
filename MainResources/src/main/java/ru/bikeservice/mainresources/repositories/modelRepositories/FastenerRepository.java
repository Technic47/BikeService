package ru.bikeservice.mainresources.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.repositories.abstracts.AbstractShowableEntityRepository;

@Repository
public interface FastenerRepository extends AbstractShowableEntityRepository<Fastener> {
}
