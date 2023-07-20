package ru.kuznetsov.bikeService.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractShowableEntityRepository;

@Repository
public interface ToolRepository extends AbstractShowableEntityRepository<Tool> {
}
