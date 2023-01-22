package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.services.ManufacturerShowableService;

@Controller
@RequestMapping("/manufacturers")
public class ManufactorerController extends BasicController<Manufacturer, ManufacturerShowableService> {
    public ManufactorerController(ManufacturerShowableService service) {
        super(service);
        this.setCurrentClass(Manufacturer.class);
    }
}
