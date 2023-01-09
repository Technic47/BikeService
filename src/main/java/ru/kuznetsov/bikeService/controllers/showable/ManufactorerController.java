package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

@Controller
@Scope("prototype")
@RequestMapping("/manufacturers")
public class ManufactorerController extends BasicController<Manufacturer> {
    public ManufactorerController(DAO<Manufacturer> dao) {
        super(dao);
        this.setCurrentClass(Manufacturer.class);
    }
}
