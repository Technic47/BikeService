package ru.bikeservice.controllers.notRest.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.BasicController;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.services.modelServices.ManufacturerService;

@Controller
@RequestMapping("/manufacturers")
public class ManufactorerController extends BasicController<Manufacturer, ManufacturerService> {
    public ManufactorerController(ManufacturerService service) {
        super(service);
        this.setCurrentClass(Manufacturer.class);
    }
}
