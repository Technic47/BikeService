package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.BasicController;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;

@Controller
@RequestMapping("/manufacturers")
public class ManufactorerController extends BasicController<Manufacturer, ManufacturerService> {
    public ManufactorerController(ManufacturerService service) {
        super(service);
        this.setCurrentClass(Manufacturer.class);
    }
}
