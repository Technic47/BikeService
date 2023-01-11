package ru.kuznetsov.bikeService.controllers.showable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.models.Showable;

import javax.validation.Valid;

@Component
public class BasicController<T extends Showable> {
    protected Class<T> currentClass;
    protected final DAO<T> dao;
    protected DAO<Picture> pictureDao;
    protected T thisObject;
    protected String currentObjectName;
    protected String category;

    public BasicController(DAO<T> dao) {
        this.dao = dao;
    }

    public void setCurrentClass(Class<T> currentClass) {
        this.currentClass = currentClass;
        this.dao.setCurrentClass(currentClass);
        this.currentObjectName = currentClass.getSimpleName().toLowerCase();
        this.category = currentObjectName + "s";
        this.dao.setTableName(category);
        try {
            assert false;
            this.thisObject = currentClass.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("objects", dao.index());
        model.addAttribute("category", category);
        return category + "/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("object", dao.show(id));
        model.addAttribute("category", category);
        model.addAttribute("properties", dao.getObjectProperties(dao.show(id)));
        return category + "/show";
    }

    @GetMapping(value = "/new")
    public String newItem(Model model) {
        model.addAttribute("properties", dao.getObjectProperties(this.thisObject));
        model.addAttribute("newObject", this.thisObject);
//        model.addAttribute("defaultPicture", pictureDao.show(0));
        return category + "/new";
    }

    @PostMapping()
    public String create(@Valid T item,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return category + "/new";
        }
        dao.save(item);
        return "redirect:/" + category;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute(currentObjectName, dao.show(id));
        return category + "/edit";
    }

    @PatchMapping("/{id}")
    public String update(@Valid T item, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return category + "/edit";
        }
        dao.update(id, item);
        return "redirect:/" + category;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        dao.del(id);
        return "redirect:/" + category;
    }

    @Autowired
    public void setPictureDAO(DAO<Picture> pictureDao) {
        this.pictureDao = pictureDao;
        this.pictureDao.setCurrentClass(Picture.class);
    }
}
