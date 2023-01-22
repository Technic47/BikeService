package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.services.ConsumableShowableService;

@Controller
@RequestMapping("/consumables")
public class ConsumableController extends UsableController<Consumable, ConsumableShowableService> {

    public ConsumableController(ConsumableShowableService service) {
        super(service);
        this.setCurrentClass(Consumable.class);
    }
}
