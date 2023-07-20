package ru.kuznetsov.bikeService.services.modelServices;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.repositories.modelRepositories.ToolRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractUsableService;

@Service
public class ToolService extends AbstractUsableService<Tool, ToolRepository> {
    public ToolService(ToolRepository repository) {
        super(repository);
    }
}
