package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.models.bike.Part;
import ru.kuznetsov.bikeService.services.PartService;

@RestController
@RequestMapping("/parts")
public class PartsController extends ServiceableController<Part, PartService> {

    public PartsController(PartService service) {
        super(service);
    }
}
