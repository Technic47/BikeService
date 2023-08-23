package ru.kuznetsov.bikeService.controllers.rest.serviceable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.ServiceableControllerREST;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;
import ru.kuznetsov.bikeService.services.modelServices.PartService;

@RestController
@RequestMapping("/api/parts")
public class PartsControllerREST extends ServiceableControllerREST<Part, PartService> {

    public PartsControllerREST(PartService service, PDFService pdfService, ManufacturerService manufacturerService) {
        super(service, pdfService, manufacturerService);
        this.setCurrentClass(Part.class);
    }
}
