package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.repositories.ManufacturerRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class ManufacturerService extends AbstractService<Manufacturer, ManufacturerRepository> {
    public ManufacturerService(ManufacturerRepository repository) {
        super(repository);
    }
}
