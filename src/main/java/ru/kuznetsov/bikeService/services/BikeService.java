package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.repositories.BikeRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractServiceableService;

@Service
public class BikeService extends AbstractServiceableService<Bike, BikeRepository> {
    public BikeService(BikeRepository repository) {
        super(repository);
    }
}
