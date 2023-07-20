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

    public List<Part> findByLinkedItemsItemIdAndLinkedItemsType(Long Item_id, String Type){
        return this.repository.findByLinkedItemsItemIdAndLinkedItemsType(Item_id, Type);
    }
}
