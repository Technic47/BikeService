package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.services.ToolShowableService;

@Controller
@RequestMapping("/tools")
public class ToolController extends UsableController<Tool, ToolShowableService> {

    public ToolController(ToolShowableService service) {
        super(service);
        this.setCurrentClass(Tool.class);
    }
}
