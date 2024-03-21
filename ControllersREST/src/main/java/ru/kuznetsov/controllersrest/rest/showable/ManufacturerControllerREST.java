package ru.kuznetsov.controllersrest.rest.showable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.kuznetsov.controllersrest.rest.BasicControllerREST;

@Tag(name = "Manufacturers", description = "The Manufacturer API")
@RestController
@RequestMapping("/api/manufacturers")
public class ManufacturerControllerREST extends BasicControllerREST<Manufacturer> {
    public ManufacturerControllerREST() {
        super();
        this.setCurrentClass(Manufacturer.class);
    }
}
