package ru.kuznetsov.bikeService.controllers.notRest.serviceable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.kuznetsov.bikeService.controllers.notRest.ServiceableController;

@Hidden
@Controller
@RequestMapping("/parts")
public class PartsController extends ServiceableController<Part> {
    public PartsController() {
        super();
        this.setCurrentClass(Part.class);
    }
}
