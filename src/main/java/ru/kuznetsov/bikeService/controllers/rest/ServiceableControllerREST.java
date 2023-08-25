package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kuznetsov.bikeService.exceptionHandlers.AccessToResourceDenied;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.abstracts.CommonServiceableEntityService;
import ru.kuznetsov.bikeService.services.modelServices.*;

import java.security.Principal;
import java.util.Map;

public abstract class ServiceableControllerREST<T extends AbstractServiceableEntity,
        S extends CommonServiceableEntityService<T>>
        extends UsableControllerREST<T, S> {
    protected DocumentService documentService;
    protected FastenerService fastenerService;
    protected ConsumableService consumableService;
    protected ToolService toolService;


    protected ServiceableControllerREST(S service, PDFService pdfService, ManufacturerService manufacturerService) {
        super(service, pdfService, manufacturerService);
    }

    @PutMapping("/{itemId}/linkedItems/{linkedItemId}")
    public AbstractEntityDto addItemToItemList(@PathVariable Long itemId,
                                               @PathVariable Long linkedItemId,
                                               @RequestParam(value = "type") String type,
                                               @RequestParam(value = "amount") Integer amount,
                                               Principal principal) {
        T item = service.getById(itemId);
        Showable itemToAdd = this.checkItem(linkedItemId, type);
        if (this.checkAccessToItem(item, principal)){
            T updated = this.itemsManipulation(item, 1, itemToAdd.getClass(), linkedItemId, amount);
            return new AbstractEntityDto(updated);
        } else throw new AccessToResourceDenied(itemToAdd.getId());
    }

    @DeleteMapping("/{itemId}/linkedItems/{linkedItemId}")
    public AbstractEntityDto delItemFromItemList(@PathVariable Long itemId,
                                               @PathVariable Long linkedItemId,
                                               @RequestParam(value = "type") String type,
                                               @RequestParam(value = "amount") Integer amount,
                                               Principal principal) {
        T item = service.getById(itemId);
        Showable itemToAdd = this.checkItem(linkedItemId, type);
        if (this.checkAccessToItem(item, principal)){
            T updated = this.itemsManipulation(item, 0, itemToAdd.getClass(), linkedItemId, amount);
            return new AbstractEntityDto(updated);
        } else throw new AccessToResourceDenied(itemToAdd.getId());
    }

    private T itemsManipulation(T item, int action, Class itemClass, Long id, int amount) {
        PartEntity entity = new PartEntity(item.getClass().getSimpleName(),
                itemClass.getSimpleName(), id, amount);
        switch (action) {
            case 1 -> {
                return service.addToLinkedItems(item, entity);
            }
            case 0 -> {
                return service.delFromLinkedItems(item, entity);
            }
            default -> throw new IllegalArgumentException("Argument 'action' is wrong!");
        }
    }

    Showable checkItem(Long id, String type) {
        switch (type) {
            case "Document", "document" -> {
                return documentService.getById(id);
            }
            case "Fastener", "fastener" -> {
                return fastenerService.getById(id);
            }
            case "Tool", "tool" -> {
                return toolService.getById(id);
            }
            case "Consumable", "consumable" -> {
                return consumableService.getById(id);
            }
            case "Part", "part" -> {
                return partService.getById(id);
            }
            default -> throw new IllegalArgumentException("Argument 'type' is wrong!");
        }
    }

    @Override
    protected void addItemAttributesShow(Map<Object, Object> response, T item, Principal principal) {
        response.put("linkedItems", item.getLinkedItems());
        super.addItemAttributesShow(response, item, principal);
    }

    //    private ServiceList getServiceList(Set<PartEntity> entityList) {
//        ServiceList serviceList = new ServiceList();
//        for (PartEntity entity : entityList) {
//            switch (entity.getType()) {
//                case "Tool" -> serviceList.addToToolMap(this.toolDAO.getById(entity.getItemId()), entity.getAmount());
//                case "Fastener" ->
//                        serviceList.addToFastenerMap(this.fastenerDAO.getById(entity.getItemId()), entity.getAmount());
//                case "Consumable" ->
//                        serviceList.addToConsumableMap(this.consumableDAO.getById(entity.getItemId()), entity.getAmount());
//                case "Document" ->
//                        serviceList.addToDocumentMap(this.documentDAO.getById(entity.getItemId()), entity.getAmount());
//                case "Part" -> serviceList.addToPartMap(this.partDAO.getById(entity.getItemId()), entity.getAmount());
//            }
//        }
//        return serviceList;
//    }
    @Autowired
    private void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Autowired
    private void setFastenerService(FastenerService fastenerService) {
        this.fastenerService = fastenerService;
    }

    @Autowired
    private void setConsumableService(ConsumableService consumableService) {
        this.consumableService = consumableService;
    }

    @Autowired
    private void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }
}
