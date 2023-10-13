package ru.bikeservice.mainresources.services.modelServices;

import org.springframework.stereotype.Service;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.repositories.modelRepositories.PartRepository;
import ru.bikeservice.mainresources.services.abstracts.AbstractServiceableService;

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
