package ru.kuznetsov.bikeService.controllers.rest.usable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.kuznetsov.bikeService.controllers.rest.UsableControllerREST;

@Tag(name = "Consumables", description = "The Consumable API")
@RestController
@RequestMapping("/api/consumables")
public class ConsumableControllerREST extends UsableControllerREST<Consumable> {
    public ConsumableControllerREST() {
        super();
        this.setCurrentClass(Consumable.class);
    }
}
