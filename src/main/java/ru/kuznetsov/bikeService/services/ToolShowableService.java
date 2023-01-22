package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.repositories.ToolRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractUsableService;

@Service
public class ToolShowableService extends AbstractUsableService<Tool, ToolRepository> {
    public ToolShowableService(ToolRepository repository) {
        super(repository);
    }
}
