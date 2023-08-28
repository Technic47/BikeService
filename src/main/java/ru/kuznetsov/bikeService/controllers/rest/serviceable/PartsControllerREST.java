package ru.kuznetsov.bikeService.controllers.rest.serviceable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.ServiceableControllerREST;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.services.modelServices.PartService;

@Tag(name = "Parts", description = "The Part API")
@RestController
@RequestMapping("/api/parts")
public class PartsControllerREST extends ServiceableControllerREST<Part, PartService> {
    public PartsControllerREST(PartService service) {
        super(service);
        this.setCurrentClass(Part.class);
    }
}
