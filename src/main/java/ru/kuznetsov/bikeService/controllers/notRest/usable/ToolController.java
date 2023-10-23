package ru.kuznetsov.bikeService.controllers.notRest.usable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.kuznetsov.bikeService.controllers.notRest.UsableController;

@Hidden
@Controller
@RequestMapping("/tools")
public class ToolController extends UsableController<Tool> {
    public ToolController() {
        super();
        this.setCurrentClass(Tool.class);
    }
}
