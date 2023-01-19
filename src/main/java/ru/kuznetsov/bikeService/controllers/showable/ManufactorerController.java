package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.services.ManufacturerService;

@RestController
@RequestMapping("/manufacturers")
public class ManufactorerController extends BasicController<Manufacturer, ManufacturerService> {
    public ManufactorerController(ManufacturerService service) {
        super(service);
    }
}
