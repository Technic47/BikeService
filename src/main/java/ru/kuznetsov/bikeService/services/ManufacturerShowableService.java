package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.repositories.ManufacturerRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractShowableService;

@Service
public class ManufacturerShowableService extends AbstractShowableService<Manufacturer, ManufacturerRepository> {
    public ManufacturerShowableService(ManufacturerRepository repository) {
        super(repository);
    }
}
