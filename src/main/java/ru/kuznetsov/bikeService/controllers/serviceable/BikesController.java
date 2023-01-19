package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.bike.Bike;
import ru.kuznetsov.bikeService.repositories.services.CommonService;

@Controller
@Scope("prototype")
@RequestMapping("/bikes")
public class BikesController extends ServiceableController<Bike> {

    public BikesController(CommonService<Bike> dao) {
        super(dao);
        this.setCurrentClass(Bike.class);
    }
}
