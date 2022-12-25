package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

@Controller
@Scope("prototype")
@RequestMapping("/consumables")
public class ConsumableController extends UsableController<Consumable> {

    public ConsumableController(DAO<Consumable> dao, DAO<Manufacturer> dao2) {
        super(dao, dao2, "consumable", new Consumable());
        this.setCurrentClass(Consumable.class);
    }
}
