package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.pictures.PictureWork;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Usable;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractShowableEntityService;

import javax.validation.Valid;
import java.security.Principal;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;

@Component
@Scope("prototype")
public class BasicController<T extends AbstractShowableEntity & Showable, S extends CommonAbstractShowableEntityService<T>> {
    protected final CommonAbstractShowableEntityService<T> dao;
    protected UserService userService;
    protected PictureService pictureDao;
    protected T thisObject;
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
            assert false;
            this.thisObject = currentClass.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @GetMapping()
    public String index(Principal principal, Model model) {
        this.user = userService.findByName(principal.getName());
        Iterable<T> objects = null;
        if (user.getStatus().contains(ROLE_USER)) {
            objects = dao.findByCreator(user.getId());
        }
        if (user.getStatus().contains(ROLE_ADMIN)) {
            objects = dao.index();
        }
        model.addAttribute("user", user.getUsername());
        model.addAttribute("objects", objects);
        model.addAttribute("category", category);
        return "/show/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Showable currentObject = dao.show(id);
        String userRole;
        if (user.getStatus().contains(ROLE_ADMIN)) {
            userRole = "admin";
        } else userRole = "user";
        model.addAttribute("userRole", userRole);
        model.addAttribute("picture", pictureDao.show(currentObject.getPicture()).getName());
        model.addAttribute("category", category);
        model.addAttribute("properties", dao.getObjectProperties(currentObject));
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
        model.addAttribute("properties", dao.getObjectProperties(this.thisObject));
        model.addAttribute("category", category);
        model.addAttribute("allPictures", pictureDao.index());
        switch (category) {
            case "parts", "bikes" -> {
                model.addAttribute("serviceObject", (Serviceable) thisObject);
                return "/new/newPart";
            }
            case "tools", "consumables" -> {
                model.addAttribute("usableObject", (Usable) thisObject);
                return "/new/newUsable";
            }
            case "documents", "fasteners", "manufacturers" -> {
                model.addAttribute("showableObject", (Showable) thisObject);
                return "/new/new";
            }
        }
        return "/new";
    }

    @PostMapping()
    public String create(@Valid T item,
                         BindingResult bindingResult
            , @RequestPart("newImage") MultipartFile file
    ) {
        if (bindingResult.hasErrors()) {
            return category + "/new";
        }
        if (!file.isEmpty()) {
            PictureWork picWorker = new PictureWork(new Picture());
            picWorker.managePicture(file);
            item.setPicture(pictureDao.save(picWorker.getPicture()).getId());
        }
        item.setCreator(user.getId());
        userService.addCreatedItem(user, new UserEntity(thisObject.getClass().getSimpleName(), dao.save(item).getId()));

        return "redirect:/" + category;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        T currentObject = dao.show(id);
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
        return "/{id}/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@Valid T item, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return category + "/edit";
        }
        dao.update(id, item);
        return "redirect:/" + category;
    }

    @PostMapping(value = "/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delCreatedItem(user, new UserEntity(thisObject.getClass().getSimpleName(), id));
        dao.delete(id);
        return "redirect:/" + category;
    }

    @Autowired
    public void setPictureDAO(PictureService pictureDao) {
        this.pictureDao = pictureDao;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
