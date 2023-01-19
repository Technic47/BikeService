package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.repositories.services.CommonService;

@Controller
@Scope("prototype")
@RequestMapping("/manufacturers")
public class ManufactorerController extends BasicController<Manufacturer> {
    public ManufactorerController(CommonService<Manufacturer> dao) {
        super(dao);
        this.setCurrentClass(Manufacturer.class);
    }
}
