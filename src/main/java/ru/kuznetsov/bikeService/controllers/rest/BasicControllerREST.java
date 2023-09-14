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
import ru.kuznetsov.bikeService.models.fabric.EntitySupportService;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static ru.kuznetsov.bikeService.models.fabric.EntitySupportService.*;


public abstract class BasicControllerREST<T extends AbstractShowableEntity,
        S extends CommonAbstractEntityService<T>>
        extends CommonEntityController {
    protected final S service;
    protected T thisClassNewObject;
    protected String category;
//    protected WebClient pdfWebClient;
//    public Connection connection;

    protected BasicControllerREST(S service) {
        this.service = service;
//        this.setSubscription();
    }

//    private void setSubscription() {
//        Dispatcher dispatcher = connection.createDispatcher();
//        dispatcher.subscribe(SUBSCRIBER_PDF);
//    }

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
            @ApiResponse(responseCode = "200", description = "Index is ok",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))})})
    @GetMapping()
    public List<AbstractEntityDto> index(Principal principal,
                                         @RequestParam(name = "shared", required = false, defaultValue = "false") boolean shared,
                                         @RequestParam(name = "searchValue", required = false) String searchValue,
                                         @RequestParam(name = "sort", required = false, defaultValue = "") String sort,
                                         @RequestParam(name = "findBy", required = false, defaultValue = "standard") String findBy) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        List<T> objects;
        if (searchValue != null) {
            try {
                objects = (List<T>) searchService.doSearchProcedure(findBy, searchValue, userModel, shared, category);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else objects = doIndexProcedure(service, userModel, category, shared);

        List<T> sortedList = sortBasic(objects, sort);

        return convertListToDto(sortedList);
    }

    @Operation(summary = "Create a new entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbstractEntityDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request Body",
                    content = @Content)})
    @PostMapping()
    public AbstractEntityDto create(@RequestBody AbstractEntityDtoNew itemDto,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        T item = EntitySupportService.convertFromDTO(category, itemDto);
        T createdItem = this.doCreateProcedure(item, service, file, principal);
        return createDtoFrom(createdItem);
    }


    @Operation(summary = "Get entity by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity is found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AbstractEntityDto.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public AbstractEntityDto show(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        T show = this.show(item, principal);
        return createDtoFrom(show);
    }

    T show(T item, Principal principal) {
        if (checkAccessToItem(item, principal)) {
            return this.doShowProcedure(item, principal);
        } else throw new AccessToResourceDenied(item.getId());
    }

    @Operation(summary = "Update entity")
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
    @PutMapping("/{id}")
    public AbstractEntityDto update(@PathVariable Long id,
                                    @RequestBody AbstractEntityDtoNew newItem,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        T item = service.getById(id);
        T newEntity = convertFromDTO(category, newItem);
        T updated = this.update(newEntity, file, item, principal);
        return createDtoFrom(updated);
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
                    content = @Content)})
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
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)})
    @GetMapping(value = "/{id}/pdf")
    @ResponseBody
    public ResponseEntity<Resource> createPdf(@PathVariable Long id, Principal principal) {
        T item = this.service.getById(id);
        return this.prepareShowablePDF(item, principal);
    }
}
