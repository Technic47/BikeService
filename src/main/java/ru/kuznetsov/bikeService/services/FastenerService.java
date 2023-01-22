package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.repositories.FastenerRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractShowableService;

@Service
public class FastenerService extends AbstractShowableService<Fastener, FastenerRepository> {

    public FastenerService(FastenerRepository repository) {
        super(repository);
    }
}
