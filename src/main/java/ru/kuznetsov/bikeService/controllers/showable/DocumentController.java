package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.services.DocumentService;

@Controller
@RequestMapping("/documents")
public class DocumentController extends BasicController<Document, DocumentService> {
    public DocumentController(DocumentService service) {
        super(service);
        this.setCurrentClass(Document.class);
    }
}
