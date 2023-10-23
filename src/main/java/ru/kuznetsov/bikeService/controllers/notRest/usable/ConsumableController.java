package ru.kuznetsov.bikeService.controllers.notRest.usable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.kuznetsov.bikeService.controllers.notRest.UsableController;

@Hidden
@Controller
@RequestMapping("/consumables")
public class ConsumableController extends UsableController<Consumable> {
    public ConsumableController() {
        super();
        this.setCurrentClass(Consumable.class);
    }
}
