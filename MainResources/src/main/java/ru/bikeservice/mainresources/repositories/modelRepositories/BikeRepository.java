package ru.bikeservice.mainresources.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.repositories.abstracts.AbstractServiceableEntityRepository;

import java.util.List;

@Repository
public interface BikeRepository extends AbstractServiceableEntityRepository<Bike> {
    List<Bike> findByLinkedPartsItemIdAndLinkedPartsType(Long Item_id, String Type);
}
