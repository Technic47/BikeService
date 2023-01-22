package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.repositories.ConsumableRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractUsableService;

@Service
public class ConsumableShowableService extends AbstractUsableService<Consumable, ConsumableRepository> {

    public ConsumableShowableService(ConsumableRepository repository) {
        super(repository);
    }
}
