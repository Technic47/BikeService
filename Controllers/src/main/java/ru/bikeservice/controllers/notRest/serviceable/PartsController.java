package ru.bikeservice.controllers.notRest.serviceable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.ServiceableController;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.services.modelServices.PartService;

@Controller
@RequestMapping("/parts")
public class PartsController extends ServiceableController<Part, PartService> {
    public PartsController(PartService service) {
        super(service);
        this.setCurrentClass(Part.class);
    }
}
