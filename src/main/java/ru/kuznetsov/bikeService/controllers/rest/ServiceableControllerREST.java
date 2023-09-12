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
import ru.kuznetsov.bikeService.config.ServiceListController;
import ru.kuznetsov.bikeService.customExceptions.AccessToResourceDenied;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.dto.PdfEntityDto;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonServiceableEntityService;

import java.io.IOException;
import java.security.Principal;

import static ru.kuznetsov.bikeService.models.fabric.EntitySupportService.createDtoFrom;

public abstract class ServiceableControllerREST<T extends AbstractServiceableEntity,
        S extends CommonServiceableEntityService<T>>
        extends UsableControllerREST<T, S> {
    private ServiceListController serviceListController;

    protected ServiceableControllerREST(S service) {
        super(service);
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
        T item = service.getById(itemId);
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
        T item = service.getById(itemId);
        Showable itemToAdd = this.checkItem(linkedItemId, type);
        if (this.checkAccessToItem(item, principal)) {
            T updated = this.itemsManipulation(item, 0, itemToAdd.getClass(), linkedItemId, amount);
            return createDtoFrom(updated);
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
    public ResponseEntity<Resource> createPdf(@PathVariable Long id, Principal principal) {
        T item = this.service.getById(id);
        UserModel userModel = getUserModelFromPrincipal(principal);
        Manufacturer manufacturer = manufacturerService.getById(item.getManufacturer());
        ServiceList serviceList = serviceListController.getServiceList(item.getLinkedItems());

        PdfEntityDto body = new PdfEntityDto(item, userModel.getUsername(), manufacturer.getName(), serviceList);

        return preparePDF(body);
    }

    protected void preparePDF(T item, Principal principal, ServiceList serviceList) {
        this.buildPDF(item, principal, serviceList);
    }

    private void buildPDF(T item, Principal principal, ServiceList serviceList) {
        Manufacturer manufacturer = this.manufacturerService.getById(item.getManufacturer());
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Picture picture = pictureService.getById(item.getPicture());
        this.pdfService.buildServiceable(item, userModel, picture, manufacturer, serviceList);
    }

    @Operation(summary = "Build PDF document for entity including linked items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF created",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content)})
    @GetMapping(value = "/{id}/pdfAll")
    public ResponseEntity<Resource> createPdfAll(@PathVariable Long id, Principal principal) throws IOException {
        T item = this.service.getById(id);
        UserModel userModel = getUserModelFromPrincipal(principal);
        Manufacturer manufacturer = manufacturerService.getById(item.getManufacturer());
        ServiceList serviceList = serviceListController.getGeneralServiceList(item.getLinkedItems());

        PdfEntityDto body = new PdfEntityDto(item, userModel.getUsername(), manufacturer.getName(), serviceList);

        return preparePDF(body);
    }

    @Autowired
    public void setServiceListController(ServiceListController serviceListController) {
        this.serviceListController = serviceListController;
    }
}
