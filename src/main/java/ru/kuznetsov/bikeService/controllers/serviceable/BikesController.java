package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.services.BikeShowableService;

@Controller
@RequestMapping("/bikes")
public class BikesController extends ServiceableController<Bike, BikeShowableService> {

    public BikesController(BikeShowableService service) {
        super(service);
        this.setCurrentClass(Bike.class);
    }
}
