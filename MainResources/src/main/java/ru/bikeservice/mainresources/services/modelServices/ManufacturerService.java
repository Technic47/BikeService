package ru.bikeservice.mainresources.services.modelServices;

import org.springframework.stereotype.Service;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.repositories.modelRepositories.ManufacturerRepository;
import ru.bikeservice.mainresources.services.abstracts.AbstractShowableService;

@Service
public class ManufacturerService extends AbstractShowableService<Manufacturer, ManufacturerRepository> {
    public ManufacturerService(ManufacturerRepository repository) {
        super(repository);
    }
}
