package ru.kuznetsov.bikeService.controllers.notRest.usable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.notRest.UsableController;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.services.modelServices.ConsumableService;

@Controller
@RequestMapping("/consumables")
public class ConsumableController extends UsableController<Consumable, ConsumableService> {

    public ConsumableController(ConsumableService service) {
        super(service);
        this.setCurrentClass(Consumable.class);
    }
}
