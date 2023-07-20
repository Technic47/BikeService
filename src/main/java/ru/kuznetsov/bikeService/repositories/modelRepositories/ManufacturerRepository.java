package ru.kuznetsov.bikeService.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

@Repository
public interface ManufacturerRepository extends AbstractShowableEntityRepository<Manufacturer> {
}
