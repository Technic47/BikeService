package ru.bikeservice.controllers.notRest.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.BasicController;
import ru.bikeservice.mainresources.models.showable.Document;

@Controller
@RequestMapping("/documents")
public class DocumentController extends BasicController<Document> {
    public DocumentController() {
        super();
        this.setCurrentClass(Document.class);
    }
}
