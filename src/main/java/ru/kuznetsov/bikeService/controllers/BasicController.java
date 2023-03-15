package ru.kuznetsov.bikeService.controllers;

import jakarta.validation.Valid;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.controllers.pictures.PictureWork;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;

@Component
@Scope("prototype")
public class BasicController<T extends AbstractShowableEntity, S extends CommonAbstractEntityService<T>>
        extends AbstractController {
    protected final S dao;
    protected T thisClassNewObject;
    protected T currentObject;
    protected String currentObjectName;
    protected String category;
    protected UserModel user;


    public BasicController(S dao) {
        this.dao = dao;
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
            objects = dao.findByCreator(user.getId());
            logger.info("personal " + category + " are shown to '" + user.getUsername() + "'");
        }
        if (user.getStatus().contains(ROLE_ADMIN)) {
            objects = dao.index();
            logger.info(category + " are shown to " + user.getUsername());
        }

        Map<T, String> objectMap = new HashMap<>();
        if (objects != null) {
            for (T object : objects) {
                objectMap.put(object, pictureDao.show(object.getPicture()).getName());
            }
        }
        model.addAttribute("objects", objectMap);
        model.addAttribute("category", category);
        return "/show/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Principal principal,
                       Model model) {
        this.currentObject = dao.show(id);
        model.addAttribute("picture", pictureDao.show(currentObject.getPicture()).getName());
        this.addItemAttributesShow(model, currentObject);
        this.checkUser(principal);
        logger.info(category + " " + id + " was shown to '" + user.getUsername() + "'");
        return "/show/show";
    }


    @GetMapping(value = "/new")
    public String newItem(Model model) {
        this.addItemAttributesNew(model, thisClassNewObject);
        return "/new/new";
    }

    private void addItemAttributesShow(Model model, T item){
        model.addAttribute("category", category);
        model.addAttribute("object", item);
        switch (category) {
            case "parts", "bikes" -> model.addAttribute("type", "Serviceable");
            case "tools", "consumables" -> model.addAttribute("type", "Usable");
            case "documents", "fasteners", "manufacturers" -> model.addAttribute("type", "Showable");
        }
    }

    protected void addItemAttributesNew(Model model, T item) {
        model.addAttribute("allPictures", pictureDao.index());
        this.addItemAttributesShow(model, item);

    }

    protected void addItemAttributesEdit(Model model, T item) {
        model.addAttribute("picture", pictureDao.show(currentObject.getPicture()));
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
            return "/new/new";
        }

        this.checkUser(principal);

        if (!file.isEmpty()) {
            PictureWork picWorker = new PictureWork(new Picture());
            picWorker.managePicture(file);
            item.setPicture(pictureDao.save(picWorker.getPicture()).getId());
        }
        item.setCreator(user.getId());
        userService.addCreatedItem(user, new UserEntity(thisClassNewObject.getClass().getSimpleName(), dao.save(item).getId()));
        logger.info(item + " was created by '" + user.toString());
        return "redirect:/" + category;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        this.currentObject = dao.show(id);
        this.addItemAttributesEdit(model, currentObject);

        if (Objects.equals(category, "parts") || Objects.equals(category, "bikes")) {
            return "/edit/editPart";
        }
        return "/edit/editUsable";
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
                return "/edit/editPart";
            }
            return "/edit/editUsable";
        }
        this.checkUser(principal);
        if (!file.isEmpty()) {
            PictureWork picWorker = new PictureWork(new Picture());
            picWorker.managePicture(file);
            item.setPicture(pictureDao.save(picWorker.getPicture()).getId());
        }
        dao.update(id, item);
        logger.info(item.getClass().getSimpleName() + " id:" + id + " was edited by '" + user.getUsername() + "'");
        return "redirect:/" + category;
    }

    @PostMapping(value = "/{id}")
    public String delete(@PathVariable("id") Long id,
                         Principal principal) {
        this.checkUser(principal);
        userService.delCreatedItem(user, new UserEntity(thisClassNewObject.getClass().getSimpleName(), id));
        dao.delete(id);
        logger.info(thisClassNewObject.getClass().getSimpleName() + " id:" + id + " was deleted by '" + user.getUsername() + "'");
        return "redirect:/" + category;
    }

    public void checkUser(Principal principal) {
        if (this.user == null) {
            this.user = userService.findByName(principal.getName());
        }
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
