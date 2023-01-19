package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.service.Fastener;
import ru.kuznetsov.bikeService.repositories.services.CommonService;

@Controller
@Scope("prototype")
@RequestMapping("/fasteners")
public class FastenerController extends BasicController<Fastener> {
    public FastenerController(CommonService<Fastener> dao) {
        super(dao);
        this.setCurrentClass(Fastener.class);
    }
}
