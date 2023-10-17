package ru.kuznetsov.bikeService.controllers.notRest.showable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.services.modelServices.ManufacturerService;
import ru.kuznetsov.bikeService.controllers.notRest.BasicController;

@Hidden
@Controller
@RequestMapping("/manufacturers")
public class ManufactorerController extends BasicController<Manufacturer, ManufacturerService> {
    public ManufactorerController(ManufacturerService service) {
        super(service);
        this.setCurrentClass(Manufacturer.class);
    }
}
