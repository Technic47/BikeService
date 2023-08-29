package ru.kuznetsov.bikeService.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.CommonEntityController;
import ru.kuznetsov.bikeService.customExceptions.AccessToResourceDenied;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDtoNew;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BasicControllerREST<T extends AbstractShowableEntity,
        S extends CommonAbstractEntityService<T>>
        extends CommonEntityController {
    protected final S service;
    protected T thisClassNewObject;
    protected String category;

    protected BasicControllerREST(S service) {
        this.service = service;
    }

    public void setCurrentClass(Class<T> currentClass) {
        this.category = currentClass.getSimpleName().toLowerCase() + "s";
        try {
            this.thisClassNewObject = currentClass.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Operation(summary = "Get all entities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity is found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) }),
//            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
//                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content) })
    @GetMapping()
    public List<AbstractEntityDto> index(Principal principal,
                                         @RequestParam(name = "shared", required = false, defaultValue = "false") boolean shared,
                                         @RequestParam(name = "search", required = false) String value) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        List<T> objects;
        if (value != null) {
            objects = this.doSearchProcedure(value, this.service, principal, shared, category);
        } else objects = this.buildIndexList(service, userModel, category, shared);

        return this.convertItemsToDto(objects);
    }

    @Operation(summary = "Create a new entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity is found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbstractEntityDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request Body",
                    content = @Content)})
    @PostMapping()
    public AbstractEntityDto create(@RequestBody AbstractEntityDtoNew itemDto,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        T item = this.convertFromDTO(thisClassNewObject, itemDto);
        T updatedItem = this.doCreateProcedure(item, service, file, principal);
        return new AbstractEntityDto(updatedItem);
    }


    @Operation(summary = "Get entity by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity is found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbstractEntityDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public AbstractEntityDto show(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        T show = this.show(item, principal);
        return new AbstractEntityDto(show);
    }

    T show(T item, Principal principal) {
        if (checkAccessToItem(item, principal)) {
            logger.info(category + " " + item.getId() + " was shown to '" + this.getUserModelFromPrincipal(principal).getUsername() + "'");
            return item;
        } else throw new AccessToResourceDenied(item.getId());
    }

    @Operation(summary = "Update entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity is updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbstractEntityDto.class)) }),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content) })
    @PutMapping("/{id}")
    public AbstractEntityDto update(@PathVariable Long id,
                                    @RequestBody T newItem,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        T item = service.getById(id);
        T updated = this.update(newItem, file, item, principal);
        return new AbstractEntityDto(updated);
    }

    public T update(T newItem, MultipartFile file, T oldItem, Principal principal) {
        if (checkAccessToItem(oldItem, principal)) {
            return this.doUpdateProcedure(newItem, service, oldItem, file, principal);
        } else throw new AccessToResourceDenied(oldItem.getId());
    }

    @Operation(summary = "Delete entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity is deleted",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content) })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);

        if (checkAccessToItem(item, principal)) {
            this.doDeleteProcedure(item, service, principal);
            Map<String, String> response = new HashMap<>();
            response.put("deleted", "ok");
            return ResponseEntity.ok(response);
        } else throw new AccessToResourceDenied(item.getId());
    }

    @Operation(summary = "Build PDF document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF created",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content) })
    @GetMapping(value = "/{id}/pdf")
    @ResponseBody
    public ResponseEntity<Resource> createPdf(@PathVariable Long id, Principal principal) throws IOException {
        T item = this.service.getById(id);
        this.preparePDF(item, principal);
        return this.prepareResponse(item, principal);
    }

    protected void preparePDF(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Picture picture = pictureService.getById(item.getPicture());
        this.pdfService.buildShowable(item, userModel, picture);
    }
}
