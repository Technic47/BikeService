package ru.kuznetsov.controllersrest.rest.serviceable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.kuznetsov.controllersrest.rest.ServiceableControllerREST;

@Tag(name = "Bikes", description = "The Bike API")
@RestController
@RequestMapping("/api/bikes")
public class BikesControllerREST extends ServiceableControllerREST<Bike> {
    public BikesControllerREST() {
        super();
        this.setCurrentClass(Bike.class);
    }
}
