package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.pictures.PictureWork;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;
import ru.kuznetsov.bikeService.services.abstracts.CommonService;

import javax.validation.Valid;

@Component
@Scope("prototype")
public class BasicController<T extends AbstractShowableEntity & Showable, S extends CommonService<T>> {
    protected final CommonService<T> dao;
    protected UserService userService;
    protected PictureService pictureDao;
    protected T thisObject;
    protected String currentObjectName;
    protected String category;
    protected UserModel user;


    public BasicController(S dao) {
        this.dao = dao;
    }

    @Autowired
    public void setUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.user = userService.findByName(authentication.getName());
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
    @PreAuthorize("hasRole('ADMIN')")
    public String index(Model model) {
        Iterable<T> objects = dao.index();
        model.addAttribute("objects", objects);
        model.addAttribute("category", category);
        return "/common/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Showable currentObject = dao.show(id);
        model.addAttribute("object", currentObject);
        model.addAttribute("picture", pictureDao.show(currentObject.getPicture()).getName());
        model.addAttribute("category", category);
        model.addAttribute("properties", dao.getObjectProperties(currentObject));
        if (thisObject instanceof Serviceable) {
            return "/common/showPart";
        }
        return "/common/show";
    }

    @GetMapping(value = "/new")
    public String newItem(Model model) {
        model.addAttribute("properties", dao.getObjectProperties(this.thisObject));
        model.addAttribute("newObject", this.thisObject);
        model.addAttribute("allPictures", pictureDao.index());
//        model.addAttribute("defaultPicture", pictureDao.show(1L));
        return category + "/new";
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
        dao.save(item);
        return "redirect:/" + category;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        T currentObject = dao.show(id);
        model.addAttribute(currentObjectName, currentObject);
        model.addAttribute("picture", pictureDao.show(currentObject.getPicture()));
        model.addAttribute("allPictures", pictureDao.index());
        return category + "/edit";
    }

    @PatchMapping("/{id}")
    public String update(@Valid T item, BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return category + "/edit";
        }
        dao.update(id, item);
        return "redirect:/" + category;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
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
