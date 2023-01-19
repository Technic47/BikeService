package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.models.service.Tool;
import ru.kuznetsov.bikeService.services.ToolService;

@RestController
@RequestMapping("/tools")
public class ToolController extends UsableController<Tool, ToolService> {

    public ToolController(ToolService service) {
        super(service);
    }
}
