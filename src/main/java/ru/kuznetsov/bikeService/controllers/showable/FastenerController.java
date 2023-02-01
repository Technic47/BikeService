package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.BasicController;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.services.FastenerService;

@Controller
@RequestMapping("/fasteners")
public class FastenerController extends BasicController<Fastener, FastenerService> {
    public FastenerController(FastenerService service) {
        super(service);
        this.setCurrentClass(Fastener.class);
    }
}
