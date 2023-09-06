package ru.kuznetsov.bikeService.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractServiceableEntityRepository;

import java.util.List;

@Repository
public interface PartRepository extends AbstractServiceableEntityRepository<Part> {
    List<Part> findByLinkedItemsItemIdAndLinkedItemsType(Long Item_id, String Type);
}
