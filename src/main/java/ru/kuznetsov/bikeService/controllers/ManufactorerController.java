package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

import javax.validation.Valid;

@Controller
@RequestMapping("/manufacturers")
public class ManufactorerController {
    private final DAO<Manufacturer> dao;

    public ManufactorerController(DAO<Manufacturer> dao) {
        this.dao = dao;
        this.dao.setTableName("manufacturers");
        this.dao.setCurrentClass(Manufacturer.class);
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("manufacturers", dao.index());
        return "manufacturers/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("manufacturer", dao.show(id));
        return "manufacturers/show";
    }

    @GetMapping("/new")
    public String newManufacturer(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return "manufacturers/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("manufacturer") @Valid Manufacturer item,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "manufacturers/new";
        }
        dao.save(item);
        return "redirect:/manufacturers";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("manufacturer", dao.show(id));
        return "manufacturers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("manufacturer") @Valid Manufacturer item, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "manufacturers/edit";
        }
        dao.update(id, item);
        return "redirect:/manufacturers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        dao.del(id);
        return "redirect:/manufacturers";
    }
}
