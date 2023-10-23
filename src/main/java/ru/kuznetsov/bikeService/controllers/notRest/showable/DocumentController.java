package ru.kuznetsov.bikeService.controllers.notRest.showable;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.kuznetsov.bikeService.controllers.notRest.BasicController;

@Hidden
@Controller
@RequestMapping("/documents")
public class DocumentController extends BasicController<Document> {
    public DocumentController() {
        super();
        this.setCurrentClass(Document.class);
    }
}
