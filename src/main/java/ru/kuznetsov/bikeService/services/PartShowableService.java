package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.repositories.PartRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractServiceableService;

@Service
public class PartShowableService extends AbstractServiceableService<Part, PartRepository> {
    public PartShowableService(PartRepository repository) {
        super(repository);
    }
}
