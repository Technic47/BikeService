package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.services.DocumentService;

@RestController
@RequestMapping("/documents")
public class DocumentController extends BasicController<Document, DocumentService> {
    public DocumentController(DocumentService service) {
        super(service);
    }
}
