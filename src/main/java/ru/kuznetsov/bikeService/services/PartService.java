package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.repositories.PartRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractServiceableService;

@Service
public class PartService extends AbstractServiceableService<Part, PartRepository> {
    public PartService(PartRepository repository) {
        super(repository);
    }
}
