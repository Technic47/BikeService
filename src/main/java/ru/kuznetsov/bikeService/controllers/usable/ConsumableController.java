package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.services.ConsumableService;

@RestController
@RequestMapping("/consumables")
public class ConsumableController extends UsableController<Consumable, ConsumableService> {

    public ConsumableController(ConsumableService service) {
        super(service);
    }
}
