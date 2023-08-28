package ru.kuznetsov.bikeService.controllers.rest.showable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.rest.BasicControllerREST;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.services.modelServices.DocumentService;

@Tag(name = "Documents", description = "The Document API")
@RestController
@RequestMapping("/api/documents")
public class DocumentControllerREST extends BasicControllerREST<Document, DocumentService> {
    public DocumentControllerREST(DocumentService service) {
        super(service);
        this.setCurrentClass(Document.class);
    }
}
