package ru.kuznetsov.controllersrest.rest.serviceable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.kuznetsov.controllersrest.rest.ServiceableControllerREST;

@Tag(name = "Parts", description = "The Part API")
@RestController
@RequestMapping("/api/parts")
public class PartsControllerREST extends ServiceableControllerREST<Part> {
    public PartsControllerREST() {
        super();
        this.setCurrentClass(Part.class);
    }
}
