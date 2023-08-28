package ru.kuznetsov.bikeService.controllers.rest.serviceable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.ServiceableControllerREST;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.services.modelServices.BikeService;

@RestController
@RequestMapping("/api/bikes")
public class BikesControllerREST extends ServiceableControllerREST<Bike, BikeService> {
    public BikesControllerREST(BikeService service) {
        super(service);
        this.setCurrentClass(Bike.class);
    }
}
