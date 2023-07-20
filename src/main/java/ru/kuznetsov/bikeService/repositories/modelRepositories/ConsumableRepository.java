package ru.kuznetsov.bikeService.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

@Repository
public interface ConsumableRepository extends AbstractShowableEntityRepository<Consumable> {
}
