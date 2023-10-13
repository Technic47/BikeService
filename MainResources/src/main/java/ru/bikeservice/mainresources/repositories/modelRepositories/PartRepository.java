package ru.bikeservice.mainresources.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.repositories.abstracts.AbstractServiceableEntityRepository;

import java.util.List;

@Repository
public interface PartRepository extends AbstractServiceableEntityRepository<Part> {
    List<Part> findByLinkedItemsItemIdAndLinkedItemsType(Long Item_id, String Type);
}
