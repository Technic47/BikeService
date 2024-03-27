package ru.bikeservice.controllers.notRest.usable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.UsableController;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.services.modelServices.ConsumableService;

@Controller
@RequestMapping("/consumables")
public class ConsumableController extends UsableController<Consumable, ConsumableService> {
    public ConsumableController(ConsumableService service) {
        super(service);
        this.setCurrentClass(Consumable.class);
    }
}
