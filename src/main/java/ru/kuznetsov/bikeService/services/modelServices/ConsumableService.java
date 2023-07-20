package ru.kuznetsov.bikeService.services.modelServices;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.repositories.modelRepositories.ConsumableRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractUsableService;

@Service
public class ConsumableService extends AbstractUsableService<Consumable, ConsumableRepository> {
    public ConsumableService(ConsumableRepository repository) {
        super(repository);
    }
}
