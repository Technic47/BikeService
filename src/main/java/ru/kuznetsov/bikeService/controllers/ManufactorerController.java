package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.DAO.manufactorers.ManufacturerDAO;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

import javax.validation.Valid;

@Controller
@RequestMapping("/manufacturers")
public class ManufactorerController {
    private final ManufacturerDAO manufacturerDAO;

    public ManufactorerController(ManufacturerDAO manufacturerDAO) {
        this.manufacturerDAO = manufacturerDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("manufacturers", manufacturerDAO.index());
        return "manufacturers/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("manufacturer", manufacturerDAO.show(id));
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
        manufacturerDAO.save(item);
        return "redirect:/manufacturers";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("manufacturer", manufacturerDAO.show(id));
        return "manufacturers/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("manufacturer") @Valid Manufacturer item, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "manufacturers/edit";
        }
        manufacturerDAO.update(id, item);
        return "redirect:/manufacturers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        manufacturerDAO.del(id);
        return "redirect:/manufacturers";
    }
}
