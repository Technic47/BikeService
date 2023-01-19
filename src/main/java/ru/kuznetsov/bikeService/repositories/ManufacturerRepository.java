package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

@Repository
public interface ManufacturerRepository extends CommonRepository<Manufacturer> {
}
