package ru.kuznetsov.bikeService.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.service.Fastener;

@Controller
@Scope("prototype")
@RequestMapping("/fasteners")
public class FastenerController extends BasicController<Fastener> {
    public FastenerController(DAO<Fastener> dao) {
        super(dao, "fastener", new Fastener());
        this.setCurrentClass(Fastener.class);
    }
}
