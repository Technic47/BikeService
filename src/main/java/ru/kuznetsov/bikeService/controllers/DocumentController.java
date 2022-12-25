package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.documents.Document;

import javax.validation.Valid;

@Controller
@RequestMapping("/documents")
public class DocumentController {
    private final DAO<Document> dao;
    private final String currentObject = "document";
    private final String category = currentObject + "s";

    public DocumentController(DAO<Document> dao) {
        this.dao = dao;
        this.dao.setTableName(category);
        this.dao.setCurrentClass(Document.class);
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
        model.addAttribute("properties", dao.getObjectProperties(new Document()));
        model.addAttribute("document", new Document());
        return category + "/new";
    }

    @PostMapping()
    public String create(@Valid Document item,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return category + "/new";
        }
//        model.addAttribute("object", currentObject);
        dao.save(item);
        return "redirect:/" + category;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute(currentObject, dao.show(id));
        return category + "/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute(currentObject) @Valid Document doc, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return category + "/edit";
        }
        dao.update(id, doc);
        return "redirect:/" + category;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        dao.del(id);
        return "redirect:/" + category;
    }
}
