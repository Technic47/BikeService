package ru.kuznetsov.bikeService.controllers;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.controllers.pictures.PictureWork;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Usable;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

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
        model.addAttribute("category", category);
        this.checkUser(principal);
        logger.info(category + " " + id + " was shown to '" + user.getUsername() + "'");
        switch (category) {
            case "parts", "bikes" -> {
                model.addAttribute("serviceObject", (Serviceable) currentObject);
                return "/show/showPart";
            }
            case "tools", "consumables" -> {
                model.addAttribute("usableObject", (Usable) currentObject);
                return "/show/showUsable";
            }
            case "documents", "fasteners", "manufacturers" -> {
                model.addAttribute("showableObject", (Showable) currentObject);
                return "/show/show";
            }
        }
        return "/{id}";
    }


    @GetMapping(value = "/new")
    public String newItem(Model model) {
        model.addAttribute("category", category);
        model.addAttribute("allPictures", pictureDao.index());
        switch (category) {
            case "parts", "bikes" -> {
                model.addAttribute("serviceObject", (Serviceable) thisClassNewObject);
                return "/new/newPart";
            }
            case "tools", "consumables" -> {
                model.addAttribute("usableObject", (Usable) thisClassNewObject);
                return "/new/newUsable";
            }
            case "documents", "fasteners", "manufacturers" -> {
                model.addAttribute("showableObject", (Showable) thisClassNewObject);
                return "/new/new";
            }
        }
        return category + "/new";
    }

    @PostMapping()
    public String create(@Valid T item,
                         Principal principal,
                         BindingResult bindingResult,
                         @RequestPart("newImage") MultipartFile file
    ) {
        if (bindingResult.hasErrors()) {
            return category + "/new";
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
        model.addAttribute("category", category);
        model.addAttribute("picture", pictureDao.show(currentObject.getPicture()));
        model.addAttribute("allPictures", pictureDao.index());
        switch (category) {
            case "parts", "bikes" -> {
                model.addAttribute("serviceObject", (Serviceable) currentObject);
                return "/edit/editPart";
            }
            case "tools", "consumables" -> {
                model.addAttribute("usableObject", (Usable) currentObject);
                return "/edit/editUsable";
            }
            case "documents", "fasteners", "manufacturers" -> {
                model.addAttribute("showableObject", (Showable) currentObject);
                return "/edit/edit";
            }
        }
        return category + "/{id}/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@Valid T item,
                         Principal principal,
                         BindingResult bindingResult,
                         @RequestPart(value = "newImage") MultipartFile file,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return category + "/edit";
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
}
