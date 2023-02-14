package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.repositories.PartRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractServiceableService;
import ru.kuznetsov.bikeService.services.abstracts.CommonServiceableEntityService;

@Service
public class PartService extends AbstractServiceableService<Part, PartRepository> implements CommonServiceableEntityService<Part> {
    public PartService(PartRepository repository) {
        super(repository);
    }
}
