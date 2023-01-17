package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

@Controller
@Scope("prototype")
@RequestMapping("/documents")
public class DocumentController extends BasicController<Document, CommonRepository<Document>> {
    public DocumentController(DAO<Document> dao) {
        super(dao);
        this.setCurrentClass(Document.class);
    }
}
