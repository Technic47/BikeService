package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.DAO.fasteners.FastenerDAO;
import ru.kuznetsov.bikeService.models.service.Fastener;

import javax.validation.Valid;

@Controller
@RequestMapping("/fasteners")
public class FastenerController {
    private final FastenerDAO fastenerDAO;

    public FastenerController(FastenerDAO fastenerDAO) {
        this.fastenerDAO = fastenerDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("fasteners", fastenerDAO.index());
        return "fasteners/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("fastener", fastenerDAO.show(id));
        return "fasteners/show";
    }

    @GetMapping("/new")
    public String newFastener(Model model) {
        model.addAttribute("fastener", new Fastener());
        return "fasteners/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("fastener") @Valid Fastener item,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "fasteners/new";
        }
        fastenerDAO.save(item);
        return "redirect:/fasteners";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("fastener", fastenerDAO.show(id));
        return "fasteners/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("fastener") @Valid Fastener item, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "fasteners/edit";
        }
        fastenerDAO.update(id, item);
        return "redirect:/fasteners";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        fastenerDAO.del(id);
        return "redirect:/fasteners";
    }
}
