package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.controllers.usable.UsableController;
import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.bike.Serviceable;
import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.models.service.Fastener;
import ru.kuznetsov.bikeService.models.service.Tool;
import ru.kuznetsov.bikeService.models.service.Usable;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceableController<T extends Serviceable & Usable> extends UsableController<T> {
    protected DAO<ServiceList> daoServiceList;
    protected DAO<Document> documentDAO;
    protected DAO<Fastener> fastenerDAO;
    protected DAO<Consumable> consumableDAO;
    protected DAO<Tool> toolDAO;
    protected ServiceList cacheList;

    public ServiceableController(DAO<T> dao) {
        super(dao);
    }
/*

 */
    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        this.cacheList = dao.show(id).getServiceList();

        List<Showable> documentsList = new ArrayList<>();
        List<Showable> fastenersList = new ArrayList<>();
        List<Showable> toolsList = new ArrayList<>();
        List<Showable> consumablesList = new ArrayList<>();

        for (Integer item : cacheList.getDocsList()) {
            documentsList.add(documentDAO.show(item));
        }

        for (Integer item : cacheList.getFastenerList()) {
            fastenersList.add(fastenerDAO.show(item));
        }

        for (Integer item : cacheList.getToolList()) {
            toolsList.add(toolDAO.show(item));
        }

        for (Integer item : cacheList.getConsumableList()) {
            consumablesList.add(consumableDAO.show(item));
        }

        model.addAttribute("documents", documentsList);
        model.addAttribute("fasteners", fastenersList);
        model.addAttribute("tools", toolsList);
        model.addAttribute("consumables", consumablesList);
        return super.show(id, model);
    }

    @Override
    public String newItem(Model model) {
        model.addAttribute("documents", documentDAO.index());
        model.addAttribute("fasteners", fastenerDAO.index());
        model.addAttribute("tools", toolDAO.index());
        model.addAttribute("consumables", consumableDAO.index());
        return super.newItem(model);
    }

    @Autowired
    public void setDaoServiceList(DAO<ServiceList> daoServiceList) {
        this.daoServiceList = daoServiceList;
        this.daoServiceList.setCurrentClass(ServiceList.class);
        this.daoServiceList.setTableName("servicelists");
    }

    @Autowired
    public void setDocumentDAO(DAO<Document> documentDAO) {
        this.documentDAO = documentDAO;
        this.documentDAO.setCurrentClass(Document.class);
        this.documentDAO.setTableName("documents");
    }

    @Autowired
    public void setFastenerDAO(DAO<Fastener> fastenerDAO) {
        this.fastenerDAO = fastenerDAO;
        this.fastenerDAO.setCurrentClass(Fastener.class);
        this.fastenerDAO.setTableName("fasteners");
    }

    @Autowired
    public void setConsumableDAO(DAO<Consumable> consumableDAO) {
        this.consumableDAO = consumableDAO;
        this.consumableDAO.setCurrentClass(Consumable.class);
        this.consumableDAO.setTableName("consumables");
    }

    @Autowired
    public void setToolDAO(DAO<Tool> toolDAO) {
        this.toolDAO = toolDAO;
        this.toolDAO.setCurrentClass(Tool.class);
        this.toolDAO.setTableName("tools");
    }
}
