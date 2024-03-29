package ru.kuznetsov.bikeService.controllers.rest.usable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.UsableControllerREST;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.services.modelServices.ToolService;

@Tag(name = "Tools", description = "The Tool API")
@RestController
@RequestMapping("/api/tools")
public class ToolControllerREST extends UsableControllerREST<Tool, ToolService> {
    public ToolControllerREST(ToolService service) {
        super(service);
        this.setCurrentClass(Tool.class);
    }
}
