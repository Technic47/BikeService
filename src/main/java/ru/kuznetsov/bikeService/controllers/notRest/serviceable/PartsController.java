package ru.kuznetsov.bikeService.controllers.notRest.serviceable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.notRest.ServiceableController;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.services.modelServices.PartService;

@Hidden
@Controller
@RequestMapping("/parts")
public class PartsController extends ServiceableController<Part, PartService> {
    public PartsController(PartService service) {
        super(service);
        this.setCurrentClass(Part.class);
    }
}
