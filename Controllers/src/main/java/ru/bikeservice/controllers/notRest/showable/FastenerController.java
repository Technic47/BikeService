package ru.bikeservice.controllers.notRest.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.BasicController;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.services.modelServices.FastenerService;

@Controller
@RequestMapping("/fasteners")
public class FastenerController extends BasicController<Fastener, FastenerService> {
    public FastenerController(FastenerService service) {
        super(service);
        this.setCurrentClass(Fastener.class);
    }
}
