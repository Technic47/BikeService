//package ru.kuznetsov.bikeService.controllers;
//
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import ru.kuznetsov.bikeService.DAO.DAO;
//import ru.kuznetsov.bikeService.models.documents.Document;
//
//import javax.validation.Valid;
//
//public class BasicController<T> {
//    protected final DAO<T> dao;
//    protected T obj;
//    protected String currentObject;
//    protected final String category = currentObject + "s";
//
//    public BasicController(DAO<T> dao, String currentObject) {
//        this.dao = dao;
//        this.dao.setTableName(category);
//        this.currentObject = currentObject;
//    }
//
//    @GetMapping()
//    public String index(Model model) {
//        model.addAttribute(category, dao.index());
//        return category + "/index";
//    }
//
//    @GetMapping("/{id}")
//    public String show(@PathVariable("id") int id, Model model) {
//        model.addAttribute(currentObject, dao.show(id));
//        return category + "/show";
//    }
//
//    @GetMapping("/new")
//    public String newDocument(Model model) {
//        obj = new T.class;
//        model.addAttribute(currentObject, new Document());
//        return category + "/new";
//    }
//
//    @PostMapping()
//    public String create(@ModelAttribute(currentObject) @Valid Document doc,
//                         BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return category + "/new";
//        }
//        dao.save(doc);
//        return "redirect:/" + category;
//    }
//
//    @GetMapping("/{id}/edit")
//    public String edit(Model model, @PathVariable("id") int id) {
//        model.addAttribute(currentObject, dao.show(id));
//        return category + "/edit";
//    }
//
//    @PatchMapping("/{id}")
//    public String update(@ModelAttribute(currentObject) @Valid Document doc, BindingResult bindingResult,
//                         @PathVariable("id") int id) {
//        if (bindingResult.hasErrors()) {
//            return category + "/edit";
//        }
//        dao.update(id, doc);
//        return "redirect:/" + category;
//    }
//
//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") int id) {
//        dao.del(id);
//        return "redirect:/" + category;
//    }
//}
