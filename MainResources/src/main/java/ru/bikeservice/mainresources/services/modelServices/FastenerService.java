package ru.bikeservice.mainresources.services.modelServices;

import org.springframework.stereotype.Service;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.repositories.modelRepositories.FastenerRepository;
import ru.bikeservice.mainresources.services.abstracts.AbstractShowableService;

@Service
public class FastenerService extends AbstractShowableService<Fastener, FastenerRepository> {
    public FastenerService(FastenerRepository repository) {
        super(repository);
    }
}
