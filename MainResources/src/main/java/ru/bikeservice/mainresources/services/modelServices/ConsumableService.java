package ru.bikeservice.mainresources.services.modelServices;

import org.springframework.stereotype.Service;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.repositories.modelRepositories.ConsumableRepository;
import ru.bikeservice.mainresources.services.abstracts.AbstractUsableService;

@Service
public class ConsumableService extends AbstractUsableService<Consumable, ConsumableRepository> {
    public ConsumableService(ConsumableRepository repository) {
        super(repository);
    }
}
