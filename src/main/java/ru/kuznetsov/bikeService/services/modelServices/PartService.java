package ru.kuznetsov.bikeService.services.modelServices;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.repositories.modelRepositories.PartRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractServiceableService;

import java.util.List;

@Service
public class PartService extends AbstractServiceableService<Part, PartRepository> {
    public PartService(PartRepository repository) {
        super(repository);
    }

    /**
     * Find all linked items for Part with specified itemId.
     * @param itemId - id of Part to find.
     * @param Type - type of linked items.
     * @return List of found items.
     */
    public List<Part> findByLinkedItemsItemIdAndLinkedItemsType(Long itemId, String Type){
        return this.repository.findByLinkedItemsItemIdAndLinkedItemsType(itemId, Type);
    }
}
