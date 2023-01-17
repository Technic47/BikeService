package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.models.service.Fastener;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

@Controller
@Scope("prototype")
@RequestMapping("/fasteners")
public class FastenerController extends BasicController<Fastener, CommonRepository<Fastener>> {
    public FastenerController(DAO<Fastener> dao) {
        super(dao);
        this.setCurrentClass(Fastener.class);
    }
}
