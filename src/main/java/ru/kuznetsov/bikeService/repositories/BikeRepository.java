package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.bike.Bike;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

@Repository
public interface BikeRepository extends CommonRepository<Bike> {
}
