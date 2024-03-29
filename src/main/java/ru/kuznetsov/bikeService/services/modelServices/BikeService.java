package ru.kuznetsov.bikeService.services.modelServices;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.repositories.modelRepositories.BikeRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractServiceableService;

import java.util.List;

@Service
public class BikeService extends AbstractServiceableService<Bike, BikeRepository> {
    public BikeService(BikeRepository repository) {
        super(repository);
    }

    /**
     * Find all linked items for Bike with specified itemId.
     *
     * @param itemId - id of Bike to find.
     * @param Type   - type of linked items.
     * @return List of found items.
     */
    public List<Bike> findByLinkedPartsItemIdAndLinkedPartsType(Long itemId, String Type) {
        return this.repository.findByLinkedPartsItemIdAndLinkedPartsType(itemId, Type);
    }
}

