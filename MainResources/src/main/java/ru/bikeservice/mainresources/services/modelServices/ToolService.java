package ru.bikeservice.mainresources.services.modelServices;

import org.springframework.stereotype.Service;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.repositories.modelRepositories.ToolRepository;
import ru.bikeservice.mainresources.services.abstracts.AbstractUsableService;

@Service
public class ToolService extends AbstractUsableService<Tool, ToolRepository> {
    public ToolService(ToolRepository repository) {
        super(repository);
    }
}
