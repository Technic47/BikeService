package ru.kuznetsov.bikeService.controllers.rest.serviceable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.ServiceableControllerREST;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.modelServices.BikeService;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;

@RestController
@RequestMapping("/api/bikes")
public class BikesControllerREST extends ServiceableControllerREST<Bike, BikeService> {

    public BikesControllerREST(BikeService service, PDFService pdfService, ManufacturerService manufacturerService) {
        super(service, pdfService, manufacturerService);
        this.setCurrentClass(Bike.class);
    }
}
