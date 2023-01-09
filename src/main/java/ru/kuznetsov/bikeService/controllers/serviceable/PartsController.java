package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.bike.Part;

@Controller
@Scope("prototype")
@RequestMapping("/parts")
public class PartsController extends ServiceableController<Part> {

    public PartsController(DAO<Part> dao) {
        super(dao);
        this.setCurrentClass(Part.class);
    }
}
