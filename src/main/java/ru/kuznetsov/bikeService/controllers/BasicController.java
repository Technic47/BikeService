package ru.kuznetsov.bikeService.controllers;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.documents.Document;

import javax.validation.Valid;

public class BasicController<T> {
    protected final DAO<T> dao;
    protected String currentObject;
    protected final String category = currentObject + "s";

    public BasicController(DAO<T> dao, String currentObject) {
        this.dao = dao;
        this.dao.setTableName(category);
        this.currentObject = currentObject;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute(category, dao.index());
        return category + "/index";
    }



    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        dao.del(id);
        return "redirect:/" + category;
    }
}
