package ru.kuznetsov.bikeService.controllers.rest.showable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.kuznetsov.bikeService.controllers.rest.BasicControllerREST;

@Tag(name = "Fasteners", description = "The Fastener API")
@RestController
@RequestMapping("/api/fasteners")
public class FastenerControllerREST extends BasicControllerREST<Fastener> {
    public FastenerControllerREST() {
        super();
        this.setCurrentClass(Fastener.class);
    }
}
