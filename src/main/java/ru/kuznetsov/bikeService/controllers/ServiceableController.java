package ru.kuznetsov.bikeService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.services.*;
import ru.kuznetsov.bikeService.services.abstracts.CommonServiceableEntityService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class ServiceableController<T extends AbstractServiceableEntity, S extends CommonServiceableEntityService<T>>
        extends UsableController<T, S> {
    protected DocumentService documentDAO;
    protected FastenerService fastenerDAO;
    protected ConsumableService consumableDAO;
    protected ToolService toolDAO;
    private PartService partDAO;
    protected ServiceList cacheList;
    private List<Long> cachePartList;

    public ServiceableController(S dao) {
        super(dao);
    }

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Principal principal,
                       Model model) {
        this.updateCacheList(id);
        this.updateCachePartList(id);
        this.addItemServiceableToModel(model);
        this.addItemShowablesToModel(model);
        return super.show(id, principal, model);
    }

    @Override
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        this.updateCacheList(id);
        this.addAllShowablesToModel(model);
        this.addItemShowablesToModel(model);
        this.updateCachePartList(id);
        this.addItemServiceableToModel(model);
        this.addAllServicableToModel(model);
        return super.edit(model, id);
    }

    @RequestMapping(value = "/{id}/update")
    public String updateServiceList(@Valid T item,
                                    Principal principal,
                                    BindingResult bindingResult,
                                    @PathVariable("id") Long id,
                                    @RequestParam(value = "action") String action,
                                    @RequestParam(value = "documentId", required = false) Long documentId,
                                    @RequestParam(value = "fastenerId", required = false) Long fastenerId,
                                    @RequestParam(value = "toolId", required = false) Long toolId,
                                    @RequestParam(value = "consumableId", required = false) Long consumableId,
                                    @RequestParam(value = "partId", required = false) Long partId,
//                                    @RequestParam(value = "partIdDel", required = false) Long partIdDel,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Model model) {
        PartEntity entity;
        switch (action) {
            case "finish":
                return this.update(item, principal, bindingResult, file, id);
            case "addDocument":
                entity = new PartEntity(thisObject.getClass().getSimpleName(), Document.class.getSimpleName(), documentId, 1);
                dao.addToServiceList(item, entity);
//                item.addToServiceList(documentDAO.show(documentId));
                break;
            case "delDocument":
                entity = new PartEntity(thisObject.getClass().getSimpleName(), Document.class.getSimpleName(), documentId, 1);
                dao.delFromServiceList(item, entity);
//                item.delFromServiceList(documentDAO.show(documentId));
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
            case "addPart":
                item.addToPartList(partDAO.show(partId));
                break;
            case "delPart":
                item.delFromPartList(partDAO.show(partId));
                break;
        }
        dao.update(id, item);
        return edit(model, id);
    }

    private void updateCacheList(Long id) {
        ServiceList newCacheList = new ServiceList();
        List<PartEntity> entityList = dao.show(id).getLinkedItems();
        for (PartEntity entity : entityList) {
            newCacheList.addIdToList(entity);
        }
        this.cacheList = newCacheList;
//        this.cacheList = dao.show(id).returnServiceListObject();
    }

    private void updateCachePartList(Long id) {
        this.cachePartList = dao.show(id).returnPartListObject();
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


    private void addItemServiceableToModel(Model model) {
        model.addAttribute("parts", this.getObjectParts());
    }

    private void addAllServicableToModel(Model model) {
        model.addAttribute("allParts", partDAO.index());
    }

    private List<Document> getObjectDocuments() {
        List<Document> documentsList = new ArrayList<>();
        for (Long item : cacheList.getDocsList()) {
            documentsList.add(documentDAO.show(item));
        }
        return documentsList;
    }

    private List<Fastener> getObjectFasteners() {
        List<Fastener> fastenersList = new ArrayList<>();
        for (Long item : cacheList.getFastenerList()) {
            fastenersList.add(fastenerDAO.show(item));
        }
        return fastenersList;
    }

    private List<Tool> getObjectTools() {
        List<Tool> toolsList = new ArrayList<>();
        for (Long item : cacheList.getToolList()) {
            toolsList.add(toolDAO.show(item));
        }
        return toolsList;
    }

    private List<Consumable> getObjectConsumables() {
        List<Consumable> consumablesList = new ArrayList<>();
        for (Long item : cacheList.getConsumableList()) {
            consumablesList.add(consumableDAO.show(item));
        }
        return consumablesList;
    }

    private List<Part> getObjectParts() {
        List<Part> partsList = new ArrayList<>();
        for (Long item : cachePartList) {
            partsList.add(partDAO.show(item));
        }
        return partsList;
    }

    @Override
    public String newItem(Model model) {
        this.addAllShowablesToModel(model);
        return super.newItem(model);
    }


    @Autowired
    public void setDocumentDAO(DocumentService documentDAO) {
        this.documentDAO = documentDAO;
    }

    @Autowired
    public void setFastenerDAO(FastenerService fastenerDAO) {
        this.fastenerDAO = fastenerDAO;
    }

    @Autowired
    public void setConsumableDAO(ConsumableService consumableDAO) {
        this.consumableDAO = consumableDAO;
    }

    @Autowired
    public void setToolDAO(ToolService toolDAO) {
        this.toolDAO = toolDAO;
    }

    @Autowired
    public void setPartDAO(PartService partDAO) {
        this.partDAO = partDAO;
    }
}
