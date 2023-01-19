package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.repositories.ConsumableRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class ConsumableService extends AbstractService<Consumable, ConsumableRepository> {

    public ConsumableService(ConsumableRepository repository) {
        super(repository);
    }
}
