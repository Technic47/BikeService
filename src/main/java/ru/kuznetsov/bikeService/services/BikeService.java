package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.repositories.BikeRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractServiceableService;
import ru.kuznetsov.bikeService.services.abstracts.CommonServiceableEntityService;

@Service
public class BikeService extends AbstractServiceableService<Bike, BikeRepository> implements CommonServiceableEntityService<Bike> {
    public BikeService(BikeRepository repository) {
        super(repository);
    }
}
