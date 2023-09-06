package ru.kuznetsov.bikeService.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractUsableEntityRepository;

@Repository
public interface ConsumableRepository extends AbstractUsableEntityRepository<Consumable> {
}
