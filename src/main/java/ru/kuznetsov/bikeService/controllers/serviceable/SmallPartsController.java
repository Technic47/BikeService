package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.bike.SmallPart;

@Controller
@Scope("prototype")
@RequestMapping("/smallParts")
public class SmallPartsController extends ServiceableController<SmallPart> {

    public SmallPartsController(DAO<SmallPart> dao) {
        super(dao);
        this.setCurrentClass(SmallPart.class);
        this.setCurrentObjectName("smallPart");
        this.setThisObject(new SmallPart());
    }


}
