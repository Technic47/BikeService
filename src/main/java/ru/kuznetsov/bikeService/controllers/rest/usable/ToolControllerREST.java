package ru.kuznetsov.bikeService.controllers.rest.usable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.UsableControllerREST;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;
import ru.kuznetsov.bikeService.services.modelServices.ToolService;

@RestController
@RequestMapping("/api/tools")
public class ToolControllerREST extends UsableControllerREST<Tool, ToolService> {

    public ToolControllerREST(ToolService service, PDFService pdfService, ManufacturerService manufacturerService) {
        super(service, pdfService, manufacturerService);
        this.setCurrentClass(Tool.class);
    }
}
