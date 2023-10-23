package ru.kuznetsov.bikeService.controllers.notRest.serviceable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.kuznetsov.bikeService.controllers.notRest.ServiceableController;

@Hidden
@Controller
@RequestMapping("/bikes")
public class BikesController extends ServiceableController<Bike> {
    public BikesController() {
        super();
        this.setCurrentClass(Bike.class);
    }
}
