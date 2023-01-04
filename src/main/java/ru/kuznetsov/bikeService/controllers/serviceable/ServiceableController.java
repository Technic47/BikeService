package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        this.cacheList = dao.show(id).returnServiceListObject();
        model.addAttribute("documents", this.getObjectDocuments());
        model.addAttribute("fasteners", this.getObjectFasteners());
        model.addAttribute("tools", this.getObjectTools());
        model.addAttribute("consumables", this.getObjectConsumables());
        return super.show(id, model);
    }

    @RequestMapping(value = "/{id}/update")
    public String updateRedirect(@Valid T item, BindingResult bindingResult,
                                 @PathVariable("id") int id,
                                 @RequestParam(value = "action") String action,
                                 Model model) {
        if ("body".equals(action)) {
            return this.update(item, bindingResult, id);
        }
        return this.editServiceList(item, model, -1, -1, -1, -1);
    }

    @RequestMapping("/editServiceList")
    public String editServiceList(@Valid T object,
                                  Model model,
                              @RequestParam(value = "document") int documentId,
                              @RequestParam(value = "fastener") int fastenerId,
                              @RequestParam(value = "tool") int toolId,
                              @RequestParam(value = "consumable") int consumableId
    ) {
        this.cacheList = object.returnServiceListObject();
        model.addAttribute("documents", this.getObjectDocuments());
        model.addAttribute("fasteners", this.getObjectFasteners());
        model.addAttribute("tools", this.getObjectTools());
        model.addAttribute("consumables", this.getObjectConsumables());
        model.addAttribute("currentObject", object);
        model.addAttribute("manufacture", daoManufacturer.show(object.getManufacturer()).getName());

//        System.out.println(item.getName());
//        item.addToServiceList(documentDAO.show(documentId));
//        item.addToServiceList(fastenerDAO.show(fastenerId));
//        item.addToServiceList(toolDAO.show(toolId));
//        item.addToServiceList(consumableDAO.show(consumableId));
        return category + "/editServiceList";
    }

    private List<Document> getObjectDocuments(){
        List<Document> documentsList = new ArrayList<>();
        for (Integer item : cacheList.getDocsList()) {
            documentsList.add(documentDAO.show(item));
        }
        return documentsList;
    }

    private List<Fastener> getObjectFasteners(){
        List<Fastener> fastenersList = new ArrayList<>();
        for (Integer item : cacheList.getDocsList()) {
            fastenersList.add(fastenerDAO.show(item));
        }
        return fastenersList;
    }
    private List<Tool> getObjectTools(){
        List<Tool> toolsList = new ArrayList<>();
        for (Integer item : cacheList.getDocsList()) {
            toolsList.add(toolDAO.show(item));
        }
        return toolsList;
    }

    private List<Consumable> getObjectConsumables(){
        List<Consumable> consumablesList = new ArrayList<>();
        for (Integer item : cacheList.getDocsList()) {
            consumablesList.add(consumableDAO.show(item));
        }
        return consumablesList;
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
