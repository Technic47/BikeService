package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.DAO.documents.DocumentsDAO;
import ru.kuznetsov.bikeService.models.documents.Document;

import javax.validation.Valid;

@Controller
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentsDAO documentsDAO;

    public DocumentController(DocumentsDAO documentsDAO) {
        this.documentsDAO = documentsDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("documents", documentsDAO.index());
        return "documents/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("document", documentsDAO.show(id));
        return "documents/show";
    }

    @GetMapping("/new")
    public String newDocument(Model model) {
        model.addAttribute("document", new Document());
        return "documents/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("document") @Valid Document doc,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "documents/new";
        }
        documentsDAO.save(doc);
        return "redirect:/documents";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("document", documentsDAO.show(id));
        return "documents/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("document") @Valid Document doc, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "documents/edit";
        }
        documentsDAO.update(id, doc);
        return "redirect:/documents";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        documentsDAO.del(id);
        return "redirect:/documents";
    }
}
