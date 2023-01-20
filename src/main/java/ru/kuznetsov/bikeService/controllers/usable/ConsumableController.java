package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.services.ConsumableService;

@Controller
@RequestMapping("/consumables")
public class ConsumableController extends UsableController<Consumable, ConsumableService> {

    public ConsumableController(ConsumableService service) {
        super(service);
        this.setCurrentClass(Consumable.class);
    }
}
