package ru.kuznetsov.bikeService.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bikeservice.mainresources.customExceptions.AccessToResourceDenied;
import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDto;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.lists.ServiceList;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.kuznetsov.bikeService.controllers.ServiceListController;

import java.security.Principal;

import static ru.bikeservice.mainresources.models.support.EntitySupportService.createDtoFrom;

public abstract class ServiceableControllerREST<T extends AbstractServiceableEntity>
        extends UsableControllerREST<T> {
    private ServiceListController serviceListController;

    protected ServiceableControllerREST() {
        super();
    }

    @Operation(summary = "Add linked item to entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbstractEntityDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request Body",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content)})
    @PutMapping("/{itemId}/linkedItems/{linkedItemId}")
    public AbstractEntityDto addItemToItemList(@PathVariable Long itemId,
                                               @PathVariable Long linkedItemId,
                                               @RequestParam(value = "type") String type,
                                               @RequestParam(value = "amount") Integer amount,
                                               Principal principal) {
        T item = doShowProcedure(thisClassNewObject.getClass().getSimpleName(), itemId);
        Showable itemToAdd = this.checkItem(linkedItemId, type);
        if (this.checkAccessToItem(item, principal)) {
            T updated = this.itemsManipulation(item, 1, itemToAdd.getClass(), linkedItemId, amount);
            return createDtoFrom(updated);
        } else throw new AccessToResourceDenied(itemToAdd.getId());
    }

    @Operation(summary = "Del linked item from entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbstractEntityDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request Body",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content)})
    @DeleteMapping("/{itemId}/linkedItems/{linkedItemId}")
    public AbstractEntityDto delItemFromItemList(@PathVariable Long itemId,
                                                 @PathVariable Long linkedItemId,
                                                 @RequestParam(value = "type") String type,
                                                 @RequestParam(value = "amount") Integer amount,
                                                 Principal principal) {
        T item = doShowProcedure(thisClassNewObject.getClass().getSimpleName(), itemId);
        Showable itemToAdd = this.checkItem(linkedItemId, type);
        if (this.checkAccessToItem(item, principal)) {
            T updated = this.itemsManipulation(item, 0, itemToAdd.getClass(), linkedItemId, amount);
            return createDtoFrom(updated);
        } else throw new AccessToResourceDenied(itemToAdd.getId());
    }

    private T itemsManipulation(T item, int action, Class itemClass, Long id, int amount) {
        String type = thisClassNewObject.getClass().getSimpleName();
        PartEntity entity = new PartEntity(type,
                itemClass.getSimpleName(), id, amount);
        return (T) doLinkedListManipulation(item, type, entity, action);
//        switch (action) {
//            case 1 -> {
//                return service.addToLinkedItems(item, entity);
//            }
//            case 0 -> {
//                return service.delFromLinkedItems(item, entity);
//            }
//            default -> throw new IllegalArgumentException("Argument 'action' is wrong!");
//        }
    }

    Showable checkItem(Long id, String type) {
        switch (type) {
            case "Document", "document" -> {
                return doShowProcedure(Document.class.getSimpleName(), id);
            }
            case "Fastener", "fastener" -> {
                return doShowProcedure(Fastener.class.getSimpleName(), id);
            }
            case "Tool", "tool" -> {
                return doShowProcedure(Tool.class.getSimpleName(), id);
            }
            case "Consumable", "consumable" -> {
                return doShowProcedure(Consumable.class.getSimpleName(), id);
            }
            case "Part", "part" -> {
                return doShowProcedure(Part.class.getSimpleName(), id);
            }
            default -> throw new IllegalArgumentException("Argument 'type' is wrong!");
        }
    }

    @Override
    public ResponseEntity<Resource> createPdf(@PathVariable Long id, Principal principal) {
        T item = doShowProcedure(thisClassNewObject.getClass().getSimpleName(), id);
        ServiceList serviceList = serviceListController.getServiceList(item.getLinkedItems());

        return this.prepareServiceablePDF(item, principal, serviceList);
    }

    @Operation(summary = "Build PDF document for entity including linked items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF created",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)})
    @GetMapping(value = "/{id}/pdfAll")
    public ResponseEntity<Resource> createPdfAll(@PathVariable Long id, Principal principal) {
        T item = doShowProcedure(thisClassNewObject.getClass().getSimpleName(), id);
        ServiceList serviceList = serviceListController.getGeneralServiceList(item.getLinkedItems());

        return this.prepareServiceablePDF(item, principal, serviceList);
    }

    @Autowired
    public void setServiceListController(ServiceListController serviceListController) {
        this.serviceListController = serviceListController;
    }
}
