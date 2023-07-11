package ru.kuznetsov.bikeService.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.pictures.PictureWork;
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
import java.util.stream.Collectors;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;
import static ru.kuznetsov.bikeService.services.PDFService.PDF_DOC_NAME;

@Component
@Scope("prototype")
public class BasicController<T extends AbstractShowableEntity,
        S extends CommonAbstractEntityService<T>>
        extends AbstractController {
    protected final S service;
    protected T thisClassNewObject;
    protected T currentObject;
    protected Map<T, String> itemMap;
    protected String currentObjectName;
    protected String category;
    protected PDFService pdfService;


    public BasicController(S service) {
        this.service = service;
        this.itemMap = new HashMap<>();
    }


    public void setCurrentClass(Class<T> currentClass) {
        this.currentObjectName = currentClass.getSimpleName().toLowerCase();
        this.category = currentObjectName + "s";
        try {
            this.thisClassNewObject = currentClass.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @GetMapping()
    public String index(Model model, Principal principal) {
        Iterable<T> objects = null;
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        if (userModel.getAuthorities().contains(ROLE_USER)) {
            objects = service.findByCreatorOrShared(userModel.getId());
            logger.info("personal " + category + " are shown to '" + userModel.getUsername() + "'");
        }
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            objects = service.index();
            logger.info(category + " are shown to " + userModel.getUsername());
        }

        if (objects != null) {
            this.itemMap.clear();
            for (T object : objects) {
                this.itemMap.put(object, pictureService.show(object.getPicture()).getName());
            }
        }
        model.addAttribute("objects", this.itemMap);
        model.addAttribute("sharedCheck", false);
        this.addItemAttributesIndex(model, principal);
        return "index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Model model, Principal principal) {
        if (this.checkCurrentObject(id)) {
            return "redirect:/" + category;
        }
        boolean access = checkAccessToItem(currentObject, principal);
        model.addAttribute("picture", pictureService.show(currentObject.getPicture()).getName());
        this.addItemAttributesShow(model, currentObject, principal);
        model.addAttribute("access", access);
        logger.info(category + " " + id + " was shown to '" + this.getUserModelFromPrincipal(principal).getUsername() + "'");
        return "show";
    }

    protected boolean checkCurrentObject(Long id){
        if (!(this.currentObject == null)) {
            if (this.currentObject.getId().equals(id)) {
                return false;
            }
        }
        this.currentObject = service.show(id);
        return this.currentObject == null;
    }

    private boolean checkAccessToItem(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            return true;
        } else {
            return item.getCreator().equals(userModel.getId());
        }
    }

    @GetMapping(value = "/new")
    public String newItem(Model model, Principal principal) {
        this.addItemAttributesNew(model, thisClassNewObject, principal);
        return "new";
    }

    private void addItemAttributesIndex(Model model, Principal principal) {
        model.addAttribute("category", category);
        this.addUserToModel(model, principal);
    }

    private void addItemAttributesShow(Model model, T item, Principal principal) {
        model.addAttribute("object", item);
        switch (category) {
            case "parts", "bikes" -> model.addAttribute("type", "Serviceable");
            case "tools", "consumables" -> model.addAttribute("type", "Usable");
            case "documents", "fasteners", "manufacturers" -> model.addAttribute("type", "Showable");
        }
        this.addItemAttributesIndex(model, principal);
    }

    protected void addItemAttributesNew(Model model, T item, Principal principal) {
        model.addAttribute("allPictures", pictureService.index());
        this.addItemAttributesShow(model, item, principal);
    }

    protected void addItemAttributesEdit(Model model, T item, Principal principal) {
        model.addAttribute("picture", pictureService.show(item.getPicture()));
        this.addItemAttributesNew(model, item, principal);
    }

    @PostMapping()
    public String create(@Valid @ModelAttribute("object") T item,
                         BindingResult bindingResult,
                         @RequestPart("newImage") MultipartFile file,
                         Model model, Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            this.addItemAttributesNew(model, item, principal);
            return "new";
        }
        if (!file.isEmpty()) {
            PictureWork picWorker = new PictureWork();
            picWorker.managePicture(file);
            item.setPicture(pictureService.save(picWorker.getPicture()).getId());
        }
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        item.setCreator(userModel.getId());
        userService.addCreatedItem(userModel,
                new UserEntity(thisClassNewObject.getClass().getSimpleName(), service.save(item).getId()));
        logger.info(item + " was created by '" + userModel.getUsername());
        return "redirect:/" + category;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id, Principal principal) {
        if (this.checkCurrentObject(id)) {
            return "redirect:/" + category;
        }
        if (checkAccessToItem(this.currentObject, principal)) {
            this.addItemAttributesEdit(model, currentObject, principal);

            if (Objects.equals(category, "parts") || Objects.equals(category, "bikes")) {
                return "editPart";
            }
            return "edit";
        } else return "redirect:/" + category + "/" + id;
    }

    @PostMapping("/{id}/edit")
    public String update(@Valid @ModelAttribute("object") T item,
                         BindingResult bindingResult,
                         @RequestPart(value = "newImage") MultipartFile file,
                         @PathVariable("id") Long id,
                         Model model, Principal principal) {
        if (checkAccessToItem(this.currentObject, principal)) {
            if (bindingResult.hasErrors()) {
                this.addItemAttributesEdit(model, item, principal);
                if (Objects.equals(category, "parts") || Objects.equals(category, "bikes")) {
                    return "editPart";
                }
                return "edit";
            }
            if (!file.isEmpty()) {
                PictureWork picWorker = new PictureWork();
                picWorker.managePicture(file);
                item.setPicture(pictureService.save(picWorker.getPicture()).getId());
            }
            service.update(id, item);
            UserModel userModel = this.getUserModelFromPrincipal(principal);
            logger.info(item.getClass()
                    .getSimpleName() + " id:" + id + " was edited by '" + userModel.getUsername() + "'");
            return "redirect:/" + category;
        } else return "redirect:/" + category + "/" + id;
    }

    @PostMapping(value = "/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        if (checkAccessToItem(this.currentObject, principal)) {
            UserModel userModel = this.getUserModelFromPrincipal(principal);
            userService.delCreatedItem(userModel,
                    new UserEntity(thisClassNewObject.getClass().getSimpleName(), id));
            service.delete(id);
            logger.info(thisClassNewObject.getClass().getSimpleName() +
                    " id:" + id + " was deleted by '" + userModel.getUsername() + "'");
            return "redirect:/" + category;
        } else return "redirect:/" + category + "/" + id;
    }

    @GetMapping(value = "/pdf")
    @ResponseBody
    public ResponseEntity<Resource> createPdf(@Param("id") Long id, Principal principal) throws IOException {
        T item = this.service.show(id);

        return this.createResponse(item, principal);
    }

    protected ResponseEntity<Resource> createResponse(T item, Principal principal) throws IOException {
        this.preparePDF(item, principal);
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
                .addImage(this.pictureService.show(item.getPicture()).getName())
                .build(item);
    }

    /**
     * Searching via matching in Name and Description. Case is ignored. ResultSet is formed considering current user`s ROLE.
     * If user is ADMIN -> no filtering.
     * Else -> filtering remains shared and ID-filtered items, or just ID-filtered items.
     *
     * @param value  string to search.
     * @param shared flag for including shared items to resultSet.
     * @param model
     * @return "index" page with search results.
     */
    @GetMapping(value = "/search")
    public String search(@RequestParam(value = "value") String value,
                         @RequestParam(value = "shared", required = false) boolean shared,
                         Model model,
                         Principal principal) {
        this.itemMap.clear();

        Set<T> resultSet = new HashSet<>();
        resultSet.addAll(this.service.findByNameContainingIgnoreCase(value));
        resultSet.addAll(this.service.findByDescriptionContainingIgnoreCase(value));

        UserModel userModel = this.getUserModelFromPrincipal(principal);
        if (!userModel.getAuthorities().contains(ROLE_ADMIN)) {
            if (shared) {
                resultSet = resultSet.stream()
                        .filter(item -> item.getCreator().equals(userModel.getId())
                                || item.getIsShared())
                        .collect(Collectors.toSet());
            } else {
                resultSet = resultSet.stream()
                        .filter(item -> item.getCreator().equals(userModel.getId()))
                        .collect(Collectors.toSet());
            }
        }
        resultSet.forEach(item -> this.itemMap.put(item,
                pictureService.show(item.getPicture()).getName()));

        model.addAttribute("user", userModel);
        model.addAttribute("objects", this.itemMap);
        model.addAttribute("category", category);

        logger.info(userModel.getUsername() + " was searching " + value + " in " + category);
        return "index";
    }

    @Autowired
    public void setPdfService(PDFService pdfService) {
        this.pdfService = pdfService;
    }
}
