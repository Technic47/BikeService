package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.repositories.BikeRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractServiceableService;

import java.util.List;

@Service
public class BikeService extends AbstractServiceableService<Bike, BikeRepository> {
    public BikeService(BikeRepository repository) {
        super(repository);
    }

    public List<Bike> findByLinkedPartsItemIdAndLinkedPartsType(Long Item_id, String Type){
        return this.repository.findByLinkedPartsItemIdAndLinkedPartsType(Item_id, Type);
    }
}

