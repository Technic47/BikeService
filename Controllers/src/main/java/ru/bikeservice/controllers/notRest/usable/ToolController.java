package ru.bikeservice.controllers.notRest.usable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.UsableController;
import ru.bikeservice.mainresources.models.usable.Tool;

@Controller
@RequestMapping("/tools")
public class ToolController extends UsableController<Tool> {
    public ToolController() {
        super();
        this.setCurrentClass(Tool.class);
    }
}
