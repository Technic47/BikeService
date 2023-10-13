package ru.bikeservice.mainresources.repositories.modelRepositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.repositories.abstracts.AbstractUsableEntityRepository;

@Repository
public interface ToolRepository extends AbstractUsableEntityRepository<Tool> {
}
