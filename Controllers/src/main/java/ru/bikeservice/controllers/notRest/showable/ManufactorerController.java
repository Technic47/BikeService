package ru.bikeservice.controllers.notRest.showable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.controllers.notRest.BasicController;

@Hidden
@Controller
@RequestMapping("/manufacturers")
public class ManufactorerController extends BasicController<Manufacturer> {
    public ManufactorerController() {
        super();
        this.setCurrentClass(Manufacturer.class);
    }
}
