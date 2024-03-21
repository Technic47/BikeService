package ru.kuznetsov.controllersrest.rest.showable;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.kuznetsov.controllersrest.rest.BasicControllerREST;

@Tag(name = "Documents", description = "The Document API")
@RestController
@RequestMapping("/api/documents")
public class DocumentControllerREST extends BasicControllerREST<Document> {
    public DocumentControllerREST() {
        super();
        this.setCurrentClass(Document.class);
    }
}
