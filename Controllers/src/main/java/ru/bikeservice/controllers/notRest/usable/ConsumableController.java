package ru.bikeservice.controllers.notRest.usable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.UsableController;
import ru.bikeservice.mainresources.models.usable.Consumable;

@Controller
@RequestMapping("/consumables")
public class ConsumableController extends UsableController<Consumable> {
    public ConsumableController() {
        super();
        this.setCurrentClass(Consumable.class);
    }
}
