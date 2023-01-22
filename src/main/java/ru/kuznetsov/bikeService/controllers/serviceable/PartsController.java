package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.services.PartShowableService;

@Controller
@RequestMapping("/parts")
public class PartsController extends ServiceableController<Part, PartShowableService> {

    public PartsController(PartShowableService service) {
        super(service);
        this.setCurrentClass(Part.class);
    }
}
