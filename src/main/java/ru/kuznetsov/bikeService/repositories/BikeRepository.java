package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

@Repository
public interface BikeRepository extends AbstractShowableEntityRepository<Bike> {
}
