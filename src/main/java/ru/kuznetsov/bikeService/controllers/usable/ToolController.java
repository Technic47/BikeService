package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.models.service.Tool;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

@Controller
@Scope("prototype")
@RequestMapping("/tools")
public class ToolController extends UsableController<Tool, CommonRepository<Tool>> {

    public ToolController(DAO<Tool> dao) {
        super(dao);
        this.setCurrentClass(Tool.class);
    }
}
