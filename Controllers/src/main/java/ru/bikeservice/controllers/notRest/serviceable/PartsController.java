package ru.bikeservice.controllers.notRest.serviceable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.ServiceableController;
import ru.bikeservice.mainresources.models.servicable.Part;

@Controller
@RequestMapping("/parts")
public class PartsController extends ServiceableController<Part> {
    public PartsController() {
        super();
        this.setCurrentClass(Part.class);
    }
}
