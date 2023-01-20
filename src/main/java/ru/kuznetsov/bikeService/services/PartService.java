package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.repositories.PartRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class PartService extends AbstractService<Part, PartRepository> {
    public PartService(PartRepository repository) {
        super(repository);
    }
}
