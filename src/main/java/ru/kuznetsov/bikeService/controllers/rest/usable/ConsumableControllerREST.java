package ru.kuznetsov.bikeService.controllers.rest.usable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.UsableControllerREST;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.modelServices.ConsumableService;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;

@RestController
@RequestMapping("/api/consumables")
public class ConsumableControllerREST extends UsableControllerREST<Consumable, ConsumableService> {

    public ConsumableControllerREST(ConsumableService service, PDFService pdfService, ManufacturerService manufacturerService) {
        super(service, pdfService, manufacturerService);
        this.setCurrentClass(Consumable.class);
    }
}
