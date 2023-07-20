package ru.kuznetsov.bikeService.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

import java.util.List;

@Repository
public interface PartRepository extends AbstractShowableEntityRepository<Part> {
    List<Part> findByLinkedItemsItemIdAndLinkedItemsType(Long Item_id, String Type);
}
