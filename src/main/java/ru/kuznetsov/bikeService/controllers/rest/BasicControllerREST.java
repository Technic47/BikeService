package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.CommonEntityController;
import ru.kuznetsov.bikeService.customExceptions.AccessToResourceDenied;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;


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

    @PostMapping()
    public AbstractEntityDto create(@RequestBody T item,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        T updatedItem = this.doCreateProcedure(item, service, file, principal);
        return new AbstractEntityDto(updatedItem);
    }

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

    @DeleteMapping(value = "/{id}")
    public boolean delete(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        if (checkAccessToItem(item, principal)) {
            this.doDeleteProcedure(item, service, principal);
            return true;
        } else throw new AccessToResourceDenied(item.getId());
    }


    @GetMapping(value = "/pdf")
    @ResponseBody
    public ResponseEntity<Resource> createPdf(@Param("id") Long id, Principal principal) throws IOException {
        T item = this.service.getById(id);
        this.preparePDF(item, principal);
        return this.prepareResponse(item, principal);
    }

    protected void preparePDF(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        this.pdfService.newPDFDocument()
                .addUserName(userModel.getUsername())
                .addImage(this.pictureService.getById(item.getPicture()).getName())
                .buildShowable(item);
    }
}
