package ru.kuznetsov.bikeService.controllers.notRest.serviceable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.notRest.ServiceableController;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.services.modelServices.BikeService;

@Hidden
@Controller
@RequestMapping("/bikes")
public class BikesController extends ServiceableController<Bike, BikeService> {
    public BikesController(BikeService service) {
        super(service);
        this.setCurrentClass(Bike.class);
    }
}
