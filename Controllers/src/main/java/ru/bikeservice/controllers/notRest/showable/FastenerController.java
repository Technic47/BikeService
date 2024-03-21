package ru.bikeservice.controllers.notRest.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.BasicController;
import ru.bikeservice.mainresources.models.showable.Fastener;

@Controller
@RequestMapping("/fasteners")
public class FastenerController extends BasicController<Fastener> {
    public FastenerController() {
        super();
        this.setCurrentClass(Fastener.class);
    }
}
