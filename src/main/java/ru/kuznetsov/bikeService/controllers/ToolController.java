package ru.kuznetsov.bikeService.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.models.service.Tool;

@Controller
@Scope("prototype")
@RequestMapping("/tools")
public class ToolController extends BasicController<Tool> {
    public ToolController(DAO<Tool> dao) {
        super(dao, "tool", new Tool());
        this.setCurrentClass(Tool.class);
    }
}
