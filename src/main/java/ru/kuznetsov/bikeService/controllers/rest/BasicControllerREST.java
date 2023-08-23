package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.dto.ShowableDto;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;
import static ru.kuznetsov.bikeService.services.PDFService.PDF_DOC_NAME;


public abstract class BasicControllerREST<T extends AbstractShowableEntity,
        S extends CommonAbstractEntityService<T>>
        extends AbstractController {
    protected final S service;
    protected final PDFService pdfService;
    protected T thisClassNewObject;
    protected String category;

    protected BasicControllerREST(S service, PDFService pdfService) {
        this.service = service;
        this.pdfService = pdfService;
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
        List<T> objects = new ArrayList<>();
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Long userId = userModel.getId();

        if (userModel.getAuthorities().contains(ROLE_USER)) {
            objects = service.findByCreatorOrShared(userId);
            logger.info("personal " + category + " are shown to '" + userModel.getUsername() + "'");
        }
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            objects = service.index();
            logger.info(category + " are shown to " + userModel.getUsername());
        }

        List<T> indexList = new ArrayList<>();
        List<T> sharedIndexList = new ArrayList<>();
        if (objects != null) {
            objects.forEach(object -> {
                if (object.getCreator().equals(userId)) {
                    indexList.add(object);
                } else sharedIndexList.add(object);
            });
        }

//        Map<T, String> indexMap = new HashMap<>();
//        Map<T, String> sharedIndexMap = new HashMap<>();
//        if (objects != null) {
//            objects.forEach(object -> {
//                if (object.getCreator().equals(userId)) {
//                    indexMap.put(object, pictureService.getById(object.getPicture()).getName());
//                } else sharedIndexMap.put(object, pictureService.getById(object.getPicture()).getName());
//            });
//        }
        Map<Object, Object> response = new HashMap<>();
//        response.put("indexMap", indexMap);
//        response.put("sharedIndexMap", sharedIndexMap);
        response.put("indexList", indexList);
        response.put("sharedIndexList", sharedIndexList);
        response.put("sharedCheck", false);
        this.addItemAttributesIndex(response, principal);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public T create(@RequestBody T item,
                    @RequestPart("newImage") MultipartFile file,
                    Principal principal) {
        if (!file.isEmpty()) {
            Picture picture = pictureService.save(file);
            item.setPicture(picture.getId());
        }
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        item.setCreator(userModel.getId());
        item.setCreated(new Date());
        T createdItem = service.save(item);
        userService.addCreatedItem(userModel,
                new UserEntity(thisClassNewObject.getClass().getSimpleName(), createdItem.getId()));
        logger.info(item + " was created by '" + userModel.getUsername());
        return createdItem;
    }

    @GetMapping("/{id}")
    public ResponseEntity show(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        Map<Object, Object> response = new HashMap<>();
        if (item == null) {
            throw new RuntimeException("Item not found!");
        } else {
//            response.put("item", item);
            this.show(item, response, principal);
            return ResponseEntity.ok(response);
        }
    }

    protected void show(T item, Map<Object, Object> response, Principal principal) {
        boolean access = checkAccessToItem(item, principal);
        response.put("access", access);
        response.put("picture", pictureService.getById(item.getPicture()).getName());
        this.addItemAttributesShow(response, item, principal);
        logger.info(category + " " + item.getId() + " was shown to '" + this.getUserModelFromPrincipal(principal).getUsername() + "'");
    }

    @PutMapping("/{id}")
    public T update(@PathVariable Long id,
                                 @RequestBody T newItem,
                                 @RequestPart(value = "newImage", required = false) MultipartFile file,
                                 Principal principal) {
        T item = service.getById(id);
        Map<Object, Object> response = new HashMap<>();
        T updated = this.update(newItem, file, item, response, principal);
        return updated;
    }

    public T update(T newItem, MultipartFile file, T oldItem, Map<Object, Object> response, Principal principal) {
        if (checkAccessToItem(oldItem, principal)) {
            if (file != null) {
                Picture picture = pictureService.save(file);
                newItem.setPicture(picture.getId());
            }
            T updated = service.update(oldItem, newItem);
            response.put("updated", new ShowableDto(updated));
            UserModel userModel = this.getUserModelFromPrincipal(principal);
            logger.info(newItem.getClass()
                    .getSimpleName() + " id:" + oldItem.getId() + " was edited by '" + userModel.getUsername() + "'");
            return updated;
        } else return null;
    }

    @DeleteMapping(value = "/{id}")
    public boolean delete(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        if (checkAccessToItem(item, principal)) {
            UserModel userModel = this.getUserModelFromPrincipal(principal);
            service.delete(id);
            this.cleanUpAfterDelete(item, id, userModel);
            logger.info(category +
                    " id:" + id + " was deleted by '" + userModel.getUsername() + "'");
            return true;
        } else throw new RuntimeException("You do not have access to item with id: " + id);
    }

    private void cleanUpAfterDelete(T item, Long id, UserModel userModel) {
        String partType = item.getClass().getSimpleName();
        Runnable clearUser = () -> userService
                .delCreatedItem(userModel, new UserEntity(thisClassNewObject.getClass().getSimpleName(), id));
        Runnable clearParts = () -> {
            PartEntity entityPart = new PartEntity("Part", partType, id, 1);
            List<Part> listOfParts = this.partService.findByLinkedItemsItemIdAndLinkedItemsType(id, partType);
            listOfParts.parallelStream().forEach(part -> partService.delFromLinkedItems(part, entityPart));
        };
        Runnable clearBikes = () -> {
            PartEntity entityBike = new PartEntity("Bike", partType, id, 1);
            List<Bike> listOfBikes = this.bikeService.findByLinkedPartsItemIdAndLinkedPartsType(id, partType);
            listOfBikes.parallelStream().forEach(part -> bikeService.delFromLinkedItems(part, entityBike));
        };
        mainExecutor.submit(clearUser);
        mainExecutor.submit(clearParts);
        mainExecutor.submit(clearBikes);
    }

    @GetMapping(value = "/pdf")
    @ResponseBody
    public ResponseEntity<Resource> createPdf(@Param("id") Long id, Principal principal) throws IOException {
        T item = this.service.getById(id);

        return this.prepareResponse(item, principal);
    }

    protected ResponseEntity<Resource> prepareResponse(T item, Principal principal) throws IOException {
        this.preparePDF(item, principal);
        return this.createResponse(item);
    }

    protected ResponseEntity<Resource> createResponse(T item) throws IOException {
        File file = new File(PDF_DOC_NAME);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource
                (Files.readAllBytes(path));

        return ResponseEntity.ok().headers(this.headers(item.getClass().getSimpleName() + "_" + item.getId()))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType
                        ("application/octet-stream")).body(resource);
    }

    private HttpHeaders headers(String fileName) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName + ".pdf");
        header.add("Cache-Control", "no-cache, no-store,"
                + " must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }

    protected void preparePDF(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        this.pdfService.newPDFDocument()
                .addUserName(userModel.getUsername())
                .addImage(this.pictureService.getById(item.getPicture()).getName())
                .buildShowable(item);
    }

    private boolean checkAccessToItem(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            return true;
        } else {
            return item.getCreator().equals(userModel.getId());
        }
    }

    private void addItemAttributesShow(Map<Object, Object> response, T item, Principal principal) {
        response.put("object", item);
        switch (category) {
            case "parts", "bikes" -> response.put("type", "Serviceable");
            case "tools", "consumables" -> response.put("type", "Usable");
            case "documents", "fasteners", "manufacturers" -> response.put("type", "Showable");
        }
        this.addItemAttributesIndex(response, principal);
    }

    private void addItemAttributesIndex(Map<Object, Object> response, Principal principal) {
        response.put("category", category);
//        this.addUserToModel(model, principal);
    }

    protected void addItemAttributesNew(Map<Object, Object> response, T item, Principal principal) {
//        response.put("allPictures", pictureService.index());
        this.addItemAttributesShow(response, item, principal);
    }

    protected void addItemAttributesEdit(Map<Object, Object> response, T item, Principal principal) {
        response.put("picture", pictureService.getById(item.getPicture()));
        this.addItemAttributesNew(response, item, principal);
    }
}
