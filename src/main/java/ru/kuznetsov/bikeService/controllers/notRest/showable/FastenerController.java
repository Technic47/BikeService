package ru.kuznetsov.bikeService.controllers.notRest.showable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.services.modelServices.FastenerService;
import ru.kuznetsov.bikeService.controllers.notRest.BasicController;

@Hidden
@Controller
@RequestMapping("/fasteners")
public class FastenerController extends BasicController<Fastener, FastenerService> {
    public FastenerController(FastenerService service) {
        super(service);
        this.setCurrentClass(Fastener.class);
    }
}
