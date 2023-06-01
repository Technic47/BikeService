package ru.kuznetsov.bikeService.controllers;

import jakarta.validation.Valid;
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

@Component
@Scope("prototype")
public class BasicController<T extends AbstractShowableEntity, S extends CommonAbstractEntityService<T>>
        extends AbstractController {
    protected final S service;
    protected T thisClassNewObject;
    protected T currentObject;
    protected Map<T, String> itemMap;
    protected String currentObjectName;
    protected String category;
    protected UserModel user;


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
    public String index(Principal principal,
                        Model model) {
        this.checkUser(principal);
        Iterable<T> objects = null;
        if (user.getStatus().contains(ROLE_USER)) {
            objects = service.findByCreator(user.getId());
            logger.info("personal " + category + " are shown to '" + user.getUsername() + "'");
        }
        if (user.getStatus().contains(ROLE_ADMIN)) {
            objects = service.index();
            logger.info(category + " are shown to " + user.getUsername());
        }

        if (objects != null) {
            for (T object : objects) {
                this.itemMap.put(object, pictureService.show(object.getPicture()).getName());
            }
        }
        model.addAttribute("objects", this.itemMap);
        model.addAttribute("category", category);
        return "index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Principal principal,
                       Model model) {
        this.currentObject = service.show(id);
        model.addAttribute("picture", pictureService.show(currentObject.getPicture()).getName());
        this.addItemAttributesShow(model, currentObject);
        this.checkUser(principal);
        logger.info(category + " " + id + " was shown to '" + user.getUsername() + "'");
        return "show";
    }


    @GetMapping(value = "/new")
    public String newItem(Model model) {
        this.addItemAttributesNew(model, thisClassNewObject);
        return "new";
    }

    private void addItemAttributesShow(Model model, T item) {
        model.addAttribute("category", category);
        model.addAttribute("object", item);
        switch (category) {
            case "parts", "bikes" -> model.addAttribute("type", "Serviceable");
            case "tools", "consumables" -> model.addAttribute("type", "Usable");
            case "documents", "fasteners", "manufacturers" -> model.addAttribute("type", "Showable");
        }
    }

    protected void addItemAttributesNew(Model model, T item) {
        model.addAttribute("allPictures", pictureService.index());
        this.addItemAttributesShow(model, item);
    }

    protected void addItemAttributesEdit(Model model, T item) {
        model.addAttribute("picture", pictureService.show(item.getPicture()));
        this.addItemAttributesNew(model, item);
    }

    @PostMapping()
    public String create(@Valid @ModelAttribute("object") T item,
                         BindingResult bindingResult,
                         Principal principal,
                         @RequestPart("newImage") MultipartFile file,
                         Model model
    ) {
        if (bindingResult.hasErrors()) {
            this.addItemAttributesNew(model, item);
            return "new";
        }

        this.checkUser(principal);

        if (!file.isEmpty()) {
            PictureWork picWorker = new PictureWork();
            picWorker.managePicture(file);
            item.setPicture(pictureService.save(picWorker.getPicture()).getId());
        }
        item.setCreator(user.getId());
        userService.addCreatedItem(user,
                new UserEntity(thisClassNewObject.getClass().getSimpleName(), service.save(item).getId()));
        logger.info(item + " was created by '" + user.toString());
        return "redirect:/" + category;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        this.currentObject = service.show(id);
        this.addItemAttributesEdit(model, currentObject);

        if (Objects.equals(category, "parts") || Objects.equals(category, "bikes")) {
            return "editPart";
        }
        return "edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@Valid @ModelAttribute("object") T item,
                         BindingResult bindingResult,
                         Principal principal,
                         @RequestPart(value = "newImage") MultipartFile file,
                         @PathVariable("id") Long id,
                         Model model) {
        if (bindingResult.hasErrors()) {
            this.addItemAttributesEdit(model, item);
            if (Objects.equals(category, "parts") || Objects.equals(category, "bikes")) {
                return "editPart";
            }
            return "edit";
        }
        this.checkUser(principal);
        if (!file.isEmpty()) {
            PictureWork picWorker = new PictureWork();
            picWorker.managePicture(file);
            item.setPicture(pictureService.save(picWorker.getPicture()).getId());
        }
        service.update(id, item);
        logger.info(item.getClass()
                .getSimpleName() + " id:" + id + " was edited by '" + user.getUsername() + "'");
        return "redirect:/" + category;
    }

    @PostMapping(value = "/{id}")
    public String delete(@PathVariable("id") Long id,
                         Principal principal) {
        this.checkUser(principal);
        userService.delCreatedItem(user,
                new UserEntity(thisClassNewObject.getClass().getSimpleName(), id));
        service.delete(id);
        logger.info(thisClassNewObject.getClass().getSimpleName() +
                " id:" + id + " was deleted by '" + user.getUsername() + "'");
        return "redirect:/" + category;
    }

    @GetMapping(value = "/pdf")
    @ResponseBody
    public ResponseEntity<Resource> createPdf(@Param("id") Long id) throws IOException {
        T item = this.service.show(id);

        return this.createResponce(item);
    }

    protected ResponseEntity<Resource> createResponce(T item) throws IOException {
        this.preparePDF(item);
        File file = new File(PDF_DOC_PATH);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource
                (Files.readAllBytes(path));

        return ResponseEntity.ok().headers(this.headers(item.getName()))
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

    protected void preparePDF(T item) {
        this.pdfService.newPDFDocument()
                .addImage(this.pictureService.show(item.getPicture()).getName())
                .build(item);
    }

    /**
     * Searching matching in Name and Description. Case is ignored. ResultSet is formed considering creator`s id.
     * @param value string to search
     * @param model
     * @param principal
     * @return
     */
    @GetMapping(value = "/search")
    public String search(@RequestParam(value = "value") String value, Model model, Principal principal) {
        this.checkUser(principal);
        Set<T> resultSet = new HashSet<>(this.service.findByNameContainingIgnoreCase(value));
        resultSet.addAll(this.service.findByDescriptionContainingIgnoreCase(value));

        resultSet = resultSet.stream()
                .filter(item -> item.getCreator().equals(this.user.getId()))
                .collect(Collectors.toSet());
        this.itemMap.clear();

        resultSet.forEach(item -> this.itemMap.put(item, pictureService.show(item.getPicture()).getName()));

        model.addAttribute("objects", this.itemMap);
        model.addAttribute("category", category);

        logger.info(user.getUsername() + " was searching " + value + " in " + category);
        return "index";
    }

    private void checkUser(Principal principal) {
        if (this.user == null) {
            this.user = userService.findByName(principal.getName());
        } else {
            if (!Objects.equals(this.user.getUsername(), principal.getName())) {
                this.user = userService.findByName(principal.getName());
            }
        }
    }
}
