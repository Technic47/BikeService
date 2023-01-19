package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.models.bike.Bike;
import ru.kuznetsov.bikeService.services.BikeService;

@RestController
@RequestMapping("/bikes")
public class BikesController extends ServiceableController<Bike, BikeService> {

    public BikesController(BikeService service) {
        super(service);
    }
}
