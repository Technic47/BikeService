package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.models.service.Fastener;
import ru.kuznetsov.bikeService.services.FastenerService;

@RestController
@RequestMapping("/fasteners")
public class FastenerController extends BasicController<Fastener, FastenerService> {
    public FastenerController(FastenerService service) {
        super(service);
    }
}
