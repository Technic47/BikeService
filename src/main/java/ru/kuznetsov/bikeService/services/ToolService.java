package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.repositories.ToolRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class ToolService extends AbstractService<Tool, ToolRepository> {
    public ToolService(ToolRepository repository) {
        super(repository);
    }
}
