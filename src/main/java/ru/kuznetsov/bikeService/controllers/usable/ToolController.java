package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.models.service.Tool;

@Controller
@Scope("prototype")
@RequestMapping("/tools")
public class ToolController extends UsableController<Tool> {

    public ToolController(DAO<Tool> dao, DAO<Manufacturer> dao2) {
        super(dao, dao2, "tool", new Tool());
        this.setCurrentClass(Tool.class);
    }
}
