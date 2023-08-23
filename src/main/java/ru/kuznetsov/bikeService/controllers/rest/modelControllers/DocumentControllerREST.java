package ru.kuznetsov.bikeService.controllers.rest.modelControllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.BasicControllerREST;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.modelServices.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentControllerREST extends BasicControllerREST<Document, DocumentService> {
    public DocumentControllerREST(DocumentService service, PDFService pdfService) {
        super(service, pdfService);
        this.setCurrentClass(Document.class);
    }
}
