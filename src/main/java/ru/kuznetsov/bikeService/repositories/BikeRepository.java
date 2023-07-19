package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

import java.util.List;

@Repository
public interface BikeRepository extends AbstractShowableEntityRepository<Bike> {
    List<Bike> findByLinkedPartsItemIdAndLinkedPartsType(Long Item_id, String Type);
}
