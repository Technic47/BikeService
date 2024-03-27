package ru.bikeservice.controllers.notRest.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bikeservice.controllers.notRest.BasicController;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.services.modelServices.DocumentService;

@Controller
@RequestMapping("/documents")
public class DocumentController extends BasicController<Document, DocumentService> {
    public DocumentController(DocumentService service) {
        super(service);
        this.setCurrentClass(Document.class);
    }
}
