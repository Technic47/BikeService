package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.services.DocumentShowableService;

@Controller
@RequestMapping("/documents")
public class DocumentController extends BasicController<Document, DocumentShowableService> {
    public DocumentController(DocumentShowableService service) {
        super(service);
        this.setCurrentClass(Document.class);
    }
}
