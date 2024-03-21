package ru.bikeservice.controllers.notRest.serviceable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.ServiceableController;
import ru.bikeservice.mainresources.models.servicable.Bike;

@Controller
@RequestMapping("/bikes")
public class BikesController extends ServiceableController<Bike> {
    public BikesController() {
        super();
        this.setCurrentClass(Bike.class);
    }
}
