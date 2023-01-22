package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.repositories.BikeRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractServiceableService;

@Service
public class BikeShowableService extends AbstractServiceableService<Bike, BikeRepository> {
    public BikeShowableService(BikeRepository repository) {
        super(repository);
    }
}
