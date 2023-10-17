package ru.kuznetsov.bikeService.controllers.rest.serviceable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.services.modelServices.BikeService;
import ru.kuznetsov.bikeService.controllers.rest.ServiceableControllerREST;

@Tag(name = "Bikes", description = "The Bike API")
@RestController
@RequestMapping("/api/bikes")
public class BikesControllerREST extends ServiceableControllerREST<Bike, BikeService> {
    public BikesControllerREST(BikeService service) {
        super(service);
        this.setCurrentClass(Bike.class);
    }
}
