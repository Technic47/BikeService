package ru.bikeservice.mainresources.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.repositories.abstracts.AbstractUsableEntityRepository;

@Repository
public interface ConsumableRepository extends AbstractUsableEntityRepository<Consumable> {
}
