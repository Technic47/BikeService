package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.repositories.BikeRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class BikeService extends AbstractService<Bike, BikeRepository> {
    public BikeService(BikeRepository repository) {
        super(repository);
    }
}
