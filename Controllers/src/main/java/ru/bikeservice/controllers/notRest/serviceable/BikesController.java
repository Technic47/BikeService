package ru.bikeservice.controllers.notRest.serviceable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.ServiceableController;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.services.modelServices.BikeService;

@Controller
@RequestMapping("/bikes")
public class BikesController extends ServiceableController<Bike, BikeService> {
    public BikesController(BikeService service) {
        super(service);
        this.setCurrentClass(Bike.class);
    }
}
