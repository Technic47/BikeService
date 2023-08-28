package ru.kuznetsov.bikeService.controllers.notRest.usable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.notRest.UsableController;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.services.modelServices.ToolService;

@Hidden
@Controller
@RequestMapping("/tools")
public class ToolController extends UsableController<Tool, ToolService> {
    public ToolController(ToolService service) {
        super(service);
        this.setCurrentClass(Tool.class);
    }
}
