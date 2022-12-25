package ru.kuznetsov.bikeService.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.documents.Document;

@Controller
@Scope("prototype")
@RequestMapping("/documents")
public class DocumentController extends BasicController<Document> {
    public DocumentController(DAO<Document> dao) {
        super(dao, "document", new Document());
        this.setCurrentClass(Document.class);
    }
}
