package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.ServiceableController;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.services.PartService;

@Controller
@RequestMapping("/parts")
public class PartsController extends ServiceableController<Part, PartService> {

    public PartsController(PartService service) {
        super(service);
        this.setCurrentClass(Part.class);
    }
}
