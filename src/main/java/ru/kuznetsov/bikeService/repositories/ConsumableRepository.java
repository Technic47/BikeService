package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

@Repository
public interface ConsumableRepository extends CommonRepository<Consumable> {
}
