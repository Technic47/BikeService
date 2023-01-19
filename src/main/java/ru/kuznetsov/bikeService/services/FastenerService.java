package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.service.Fastener;
import ru.kuznetsov.bikeService.repositories.FastenerRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class FastenerService extends AbstractService<Fastener, FastenerRepository> {

    public FastenerService(FastenerRepository repository) {
        super(repository);
    }
}
