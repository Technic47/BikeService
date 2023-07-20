package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.ServiceableController;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.services.modelServices.BikeService;

@Controller
@RequestMapping("/bikes")
public class BikesController extends ServiceableController<Bike, BikeService> {

    public BikesController(BikeService service) {
        super(service);
        this.setCurrentClass(Bike.class);
    }
}
