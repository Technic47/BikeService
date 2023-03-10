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
        this.updateCacheList(id);
        addItemShowablesToModel(model);
        return super.show(id, model);
    }

    @Override
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable int id) {
        this.updateCacheList(id);
        this.addAllShowablesToModel(model);
        this.addItemShowablesToModel(model);
        return super.edit(model, id);
    }

    @RequestMapping(value = "/{id}/update")
    public String updateRedirect(@Valid T item, BindingResult bindingResult,
                                 @PathVariable("id") int id,
                                 @RequestParam(value = "action") String action,
                                 @RequestParam(value = "documentId") int documentId,
                                 @RequestParam(value = "fastenerId") int fastenerId,
                                 @RequestParam(value = "toolId") int toolId,
                                 @RequestParam(value = "consumableId") int consumableId,
                                 Model model) {
        switch (action) {
            case "finish":
                return this.update(item, bindingResult, id);
            case "addDocument":
                item.addToServiceList(documentDAO.show(documentId));
                break;
            case "delDocument":
                item.delFromServiceList(documentDAO.show(documentId));
                break;
            case "addFastener":
                item.addToServiceList(fastenerDAO.show(fastenerId));
                break;
            case "delFastener":
                item.delFromServiceList(fastenerDAO.show(fastenerId));
                break;
            case "addTool":
                item.addToServiceList(toolDAO.show(toolId));
                break;
            case "delTool":
                item.delFromServiceList(toolDAO.show(toolId));
                break;
            case "addConsumable":
                item.addToServiceList(consumableDAO.show(consumableId));
                break;
            case "delConsumable":
                item.delFromServiceList(consumableDAO.show(consumableId));
                break;
        }
        dao.update(id, item);
        return edit(model, id);
    }

    private void updateCacheList(int id) {
        this.cacheList = dao.show(id).returnServiceListObject();
    }

    private void addItemShowablesToModel(Model model) {
        model.addAttribute("documents", this.getObjectDocuments());
        model.addAttribute("fasteners", this.getObjectFasteners());
        model.addAttribute("tools", this.getObjectTools());
        model.addAttribute("consumables", this.getObjectConsumables());
    }

    private void addAllShowablesToModel(Model model) {
        model.addAttribute("allDocuments", documentDAO.index());
        model.addAttribute("allFasteners", fastenerDAO.index());
        model.addAttribute("allTools", toolDAO.index());
        model.addAttribute("allConsumables", consumableDAO.index());
    }

    private List<Document> getObjectDocuments() {
        List<Document> documentsList = new ArrayList<>();
        for (Integer item : cacheList.getDocsList()) {
            documentsList.add(documentDAO.show(item));
        }
        return documentsList;
    }

    private List<Fastener> getObjectFasteners() {
        List<Fastener> fastenersList = new ArrayList<>();
        for (Integer item : cacheList.getFastenerList()) {
            fastenersList.add(fastenerDAO.show(item));
        }
        return fastenersList;
    }

    private List<Tool> getObjectTools() {
        List<Tool> toolsList = new ArrayList<>();
        for (Integer item : cacheList.getToolList()) {
            toolsList.add(toolDAO.show(item));
        }
        return toolsList;
    }

    private List<Consumable> getObjectConsumables() {
        List<Consumable> consumablesList = new ArrayList<>();
        for (Integer item : cacheList.getConsumableList()) {
            consumablesList.add(consumableDAO.show(item));
        }
        return consumablesList;
    }


    @Override
    public String newItem(Model model) {
        this.addAllShowablesToModel(model);
        return super.newItem(model);
    }

    @Autowired
    public void setDaoServiceList(DAO<ServiceList> daoServiceList) {
        this.daoServiceList = daoServiceList;
        this.daoServiceList.setCurrentClass(ServiceList.class);
        this.daoServiceList.setTableName("serviceLists");
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
