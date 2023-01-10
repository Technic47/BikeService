package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.bike.Bike;

@Controller
@Scope("prototype")
@RequestMapping("/bikes")
public class BikesController extends ServiceableController<Bike> {

    public BikesController(DAO<Bike> dao) {
        super(dao);
        this.setCurrentClass(Bike.class);
    }
}
