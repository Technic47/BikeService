package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.CommonEntityController;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
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
    public ResponseEntity index(Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Long userId = userModel.getId();
        List<T> objects = this.buildIndexList(service, userModel, category);

        Map<Object, Object> response = new HashMap<>();

        this.addIndexListsToResponse(response, userId, objects);
        response.put("sharedCheck", false);
        this.addItemAttributesIndex(response, principal);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public T create(@RequestBody T item,
                    @RequestPart("newImage") MultipartFile file,
                    Principal principal) {
        return this.doCreateProcedure(item, service, file, principal);
    }

    @GetMapping("/{id}")
    public ResponseEntity show(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        Map<Object, Object> response = new HashMap<>();
        if (item == null) {
            throw new RuntimeException("Item not found!");
        } else {
            this.show(item, response, principal);
            return ResponseEntity.ok(response);
        }
    }

    void show(T item, Map<Object, Object> response, Principal principal) {
        boolean access = checkAccessToItem(item, principal);
        response.put("access", access);
        this.addItemAttributesShow(response, item, principal);
        logger.info(category + " " + item.getId() + " was shown to '" + this.getUserModelFromPrincipal(principal).getUsername() + "'");
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @RequestBody T newItem,
                                 @RequestPart(value = "newImage", required = false) MultipartFile file,
                                 Principal principal) {
        T item = service.getById(id);
        Map<Object, Object> response = new HashMap<>();
        this.update(newItem, file, item, response, principal);
        return ResponseEntity.ok(response);
    }

    public T update(T newItem, MultipartFile file, T oldItem, Map<Object, Object> response, Principal principal) {
        if (checkAccessToItem(oldItem, principal)) {

            T updated = this.doUpdateProcedure(newItem, service, oldItem, file, principal);

            addItemAttributesEdit(response, updated, principal);
            return updated;
        } else return null;
    }

    @DeleteMapping(value = "/{id}")
    public boolean delete(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        if (checkAccessToItem(item, principal)) {
            this.doDeleteProcedure(item, service, principal, category);
            return true;
        } else throw new RuntimeException("You do not have access to item with id: " + id);
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
//        response.put("object", item);
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
