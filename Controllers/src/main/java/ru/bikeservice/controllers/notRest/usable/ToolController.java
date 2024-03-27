package ru.bikeservice.controllers.notRest.usable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.UsableController;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.services.modelServices.ToolService;

@Controller
@RequestMapping("/tools")
public class ToolController extends UsableController<Tool, ToolService> {
    public ToolController(ToolService service) {
        super(service);
        this.setCurrentClass(Tool.class);
    }
}
