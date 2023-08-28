package ru.kuznetsov.bikeService.controllers.rest.showable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.BasicControllerREST;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;

@Tag(name = "Manufacturers", description = "The Manufacturer API")
@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerControllerREST extends BasicControllerREST<Manufacturer, ManufacturerService> {
    public ManufacturerControllerREST(ManufacturerService service) {
        super(service);
        this.setCurrentClass(Manufacturer.class);
    }
}
