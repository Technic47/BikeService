package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.service.Consumable;

@Controller
@Scope("prototype")
@RequestMapping("/consumables")
public class ConsumableController extends UsableController<Consumable> {

    public ConsumableController(DAO<Consumable> dao) {
        super(dao);
        this.setCurrentClass(Consumable.class);
    }
}
