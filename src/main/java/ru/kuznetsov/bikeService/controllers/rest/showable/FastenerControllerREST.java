package ru.kuznetsov.bikeService.controllers.rest.showable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.BasicControllerREST;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.services.modelServices.FastenerService;

@RestController
@RequestMapping("/api/fasteners")
public class FastenerControllerREST extends BasicControllerREST<Fastener, FastenerService> {
    public FastenerControllerREST(FastenerService service) {
        super(service);
        this.setCurrentClass(Fastener.class);
    }
}
