package ru.kuznetsov.bikeService.controllers;

import jakarta.validation.Valid;
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

import java.security.Principal;
import java.util.Set;

@Component
@Scope("prototype")
public class ServiceableController<T extends AbstractServiceableEntity,
        S extends CommonServiceableEntityService<T>>
        extends UsableController<T, S> {
    protected DocumentService documentDAO;
    protected FastenerService fastenerDAO;
    protected ConsumableService consumableDAO;
    protected ToolService toolDAO;
    private PartService partDAO;
    protected ServiceList cacheList;

    public ServiceableController(S dao) {
        super(dao);
    }

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Principal principal,
                       Model model) {
        this.updateCacheList(id);
        this.addLinkedItemsToModel(model);
        return super.show(id, principal, model);
    }

    @Override
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id) {
        this.updateCacheList(id);
        this.addAllItemsToModel(model);
        this.addLinkedItemsToModel(model);
        return super.edit(model, id);
    }

    @RequestMapping(value = "/{id}/update")
    public String updateServiceList(@Valid T item,
                                    BindingResult bindingResult,
                                    Principal principal,
                                    @PathVariable("id") Long id,
                                    @RequestParam(value = "action") String action,
                                    @RequestParam(value = "documentId", required = false) Long documentId,
                                    @RequestParam(value = "fastenerId", required = false) Long fastenerId,
                                    @RequestParam(value = "fastenerQuantity", required = false) String fastenerQuantity,
                                    @RequestParam(value = "toolId", required = false) Long toolId,
                                    @RequestParam(value = "consumableId", required = false) Long consumableId,
                                    @RequestParam(value = "consumableQuantity", required = false) String consumableQuantity,
                                    @RequestParam(value = "partId", required = false) Long partId,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Model model) {
        item.setLinkedItems(this.currentObject.getLinkedItems());
        switch (action) {
            case "finish":
                return this.update(item, principal, bindingResult, file, id);
            case "addDocument":
                this.itemsManipulation(item, 1, Document.class, documentId, 1);
                break;
            case "delDocument":
                this.itemsManipulation(item, 0, Document.class, documentId, 1);
                break;
            case "addFastener":
                this.itemsManipulation(item, 1, Fastener.class, fastenerId, Integer.parseInt(fastenerQuantity));
                break;
            case "delFastener":
                this.itemsManipulation(item, 0, Fastener.class, fastenerId, Integer.parseInt(fastenerQuantity));
                break;
            case "addTool":
                this.itemsManipulation(item, 1, Tool.class, toolId, 1);
                break;
            case "delTool":
                this.itemsManipulation(item, 0, Tool.class, toolId, 1);
                break;
            case "addConsumable":
                this.itemsManipulation(item, 1, Consumable.class, consumableId, Integer.parseInt(consumableQuantity));
                break;
            case "delConsumable":
                this.itemsManipulation(item, 0, Consumable.class, consumableId, Integer.parseInt(consumableQuantity));
                break;
            case "addPart":
                this.itemsManipulation(item, 1, Part.class, partId, 1);
                break;
            case "delPart":
                this.itemsManipulation(item, 0, Part.class, partId, 1);
                break;
        }
        dao.update(id, item);
        return edit(model, id);
    }

    private void itemsManipulation(T item, int action, Class itemClass, Long id, int amount) {
        PartEntity entity = new PartEntity(thisClassNewObject.getClass().getSimpleName(), itemClass.getSimpleName(), id, amount);
        switch (action) {
            case 1 -> dao.addToLinkedItems(item, entity);
            case 0 -> dao.delFromLinkedItems(item, entity);
        }
    }

    private void updateCacheList(Long id) {
        ServiceList newCacheList = new ServiceList();
        Set<PartEntity> entityList = dao.show(id).getLinkedItems();

        for (PartEntity entity : entityList) {
            switch (entity.getType()) {
                case "Tool" -> newCacheList.addToToolMap(this.toolDAO.show(entity.getItem_id()), entity.getAmount());
                case "Fastener" ->
                        newCacheList.addToFastenerMap(this.fastenerDAO.show(entity.getItem_id()), entity.getAmount());
                case "Consumable" ->
                        newCacheList.addToConsumableMap(this.consumableDAO.show(entity.getItem_id()), entity.getAmount());
                case "Document" ->
                        newCacheList.addToDocumentMap(this.documentDAO.show(entity.getItem_id()), entity.getAmount());
                case "Part" -> newCacheList.addToPartMap(this.partDAO.show(entity.getItem_id()), entity.getAmount());
            }
        }
        this.cacheList = newCacheList;
    }


    private void addLinkedItemsToModel(Model model) {
        model.addAttribute("documents", this.cacheList.getDocsMap());
        model.addAttribute("fasteners", this.cacheList.getFastenerMap());
        model.addAttribute("tools", this.cacheList.getToolMap());
        model.addAttribute("consumables", this.cacheList.getConsumableMap());
        model.addAttribute("parts", this.cacheList.getPartMap());
    }

    private void addAllItemsToModel(Model model) {
        model.addAttribute("allDocuments", documentDAO.index());
        model.addAttribute("allFasteners", fastenerDAO.index());
        model.addAttribute("allTools", toolDAO.index());
        model.addAttribute("allConsumables", consumableDAO.index());
        model.addAttribute("allParts", partDAO.index());
    }


    @Override
    public String newItem(Model model) {
        this.addAllItemsToModel(model);
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
