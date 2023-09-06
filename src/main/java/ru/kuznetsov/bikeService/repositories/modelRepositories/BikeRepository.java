package ru.kuznetsov.bikeService.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractServiceableEntityRepository;

import java.util.List;

@Repository
public interface BikeRepository extends AbstractServiceableEntityRepository<Bike> {
    List<Bike> findByLinkedPartsItemIdAndLinkedPartsType(Long Item_id, String Type);
}
