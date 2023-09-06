package ru.kuznetsov.bikeService.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.repositories.abstracts.AbstractUsableEntityRepository;

@Repository
public interface ToolRepository extends AbstractUsableEntityRepository<Tool> {
}
