package ru.kuznetsov.bikeService.controllers.rest.serviceable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.services.modelServices.PartService;
import ru.kuznetsov.bikeService.controllers.rest.ServiceableControllerREST;

@Tag(name = "Parts", description = "The Part API")
@RestController
@RequestMapping("/api/parts")
public class PartsControllerREST extends ServiceableControllerREST<Part, PartService> {
    public PartsControllerREST(PartService service) {
        super(service);
        this.setCurrentClass(Part.class);
    }
}
