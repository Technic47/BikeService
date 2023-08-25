package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.CommonEntityController;
import ru.kuznetsov.bikeService.exceptionHandlers.AccessToResourceDenied;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PDFService;
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

    protected BasicControllerREST(S service, PDFService pdfService) {
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
                                         @RequestParam(name = "include_shared", required = false) boolean include_shared) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        List<T> objects;
        if (include_shared) {
            objects = this.buildIndexListIncludeShared(service, userModel, category);
        } else objects = this.buildIndexListExcludeShared(service, userModel, category);
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
        Map<Object, Object> response = new HashMap<>();
        T show = this.show(item, response, principal);
        return new AbstractEntityDto(show);
    }

    T show(T item, Map<Object, Object> response, Principal principal) {
        if (checkAccessToItem(item, principal)) {
            logger.info(category + " " + item.getId() + " was shown to '" + this.getUserModelFromPrincipal(principal).getUsername() + "'");
            return item;
        } else throw new AccessToResourceDenied(item.getId());
//        response.put("access", access);
//        this.addItemAttributesShow(response, item, principal);
    }

    @PutMapping("/{id}")
    public AbstractEntityDto update(@PathVariable Long id,
                                    @RequestBody T newItem,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        T item = service.getById(id);
        Map<Object, Object> response = new HashMap<>();
        T updated = this.update(newItem, file, item, response, principal);
        return new AbstractEntityDto(updated);
    }

    public T update(T newItem, MultipartFile file, T oldItem, Map<Object, Object> response, Principal principal) {
        if (checkAccessToItem(oldItem, principal)) {

            T updated = this.doUpdateProcedure(newItem, service, oldItem, file, principal);

//            addItemAttributesEdit(response, updated, principal);
            return updated;
        } else throw new AccessToResourceDenied(oldItem.getId());
    }

    @DeleteMapping(value = "/{id}")
    public boolean delete(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        if (checkAccessToItem(item, principal)) {
            this.doDeleteProcedure(item, service, principal, category);
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

    protected void addItemAttributesShow(Map<Object, Object> response, T item, Principal principal) {
        response.put("name", item.getName());
        response.put("valueName", item.getValueName());
        response.put("value", item.getValue());
        response.put("description", item.getDescription());
        response.put("link", item.getLink());
        response.put("picture", pictureService.getById(item.getPicture()).getName());
        switch (category) {
            case "parts", "bikes" -> response.put("type", "Serviceable");
            case "tools", "consumables" -> response.put("type", "Usable");
            case "documents", "fasteners", "manufacturers" -> response.put("type", "Showable");
        }
        this.addItemAttributesIndex(response, principal);
    }

    private void addItemAttributesIndex(Map<Object, Object> response, Principal principal) {
        response.put("category", category);
    }

    protected void addItemAttributesNew(Map<Object, Object> response, T item, Principal principal) {
        this.addItemAttributesShow(response, item, principal);
    }

    protected void addItemAttributesEdit(Map<Object, Object> response, T item, Principal principal) {
        this.addItemAttributesNew(response, item, principal);
    }
}
