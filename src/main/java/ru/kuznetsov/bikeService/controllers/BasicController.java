package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.documents.Document;

import javax.validation.Valid;
import java.util.function.Supplier;

@Controller
public class BasicController<T> {
    private Class<T> currentClass;
    private Supplier<T> supplier;
    private final DAO<T> dao;
    private String currentObjectName = "";
    private final String category = currentObjectName + "s";

    public BasicController(DAO<T> dao) {
        this.dao = dao;
        this.dao.setTableName(category);
    }

    public void setCurrentClass(Class<T> currentClass){
        this.currentClass = currentClass;
        this.dao.setCurrentClass(currentClass);
    }

    public void setCurrentObjectName(String currentObjectName){
        this.currentObjectName = currentObjectName;
    }

    public T getNewObj(){
        return supplier.get();
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

    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("properties", dao.getObjectProperties(this.getNewObj()));
        model.addAttribute("document", this.getNewObj());
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
}
