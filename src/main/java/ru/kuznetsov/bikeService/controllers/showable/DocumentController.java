package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.repositories.services.CommonService;

@Controller
@Scope("prototype")
@RequestMapping("/documents")
public class DocumentController extends BasicController<Document> {
    public DocumentController(CommonService<Document> dao) {
        super(dao);
//        this.setDao(dao);
        this.setCurrentClass(Document.class);
    }
}
