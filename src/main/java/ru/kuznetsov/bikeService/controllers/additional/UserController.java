package ru.kuznetsov.bikeService.controllers.additional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.services.*;

import java.security.Principal;

@Controller
//@Secured("ROLE_ADMIN")
@RequestMapping("/users")
public class UserController extends AbstractController {
    private DocumentService documentService;
    private FastenerService fastenerService;
    private ManufacturerService manufacturerService;
    private ConsumableService consumableService;
    private ToolService toolService;
    private PartService partService;
    private BikeService bikeService;

    @GetMapping()
    @Secured("ROLE_ADMIN")
    public String index(Model model) {
        model.addAttribute("users", userService.index());
        return "users_index";
    }


    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public String show(@PathVariable("id") Long id, Model model) {
        this.addAllCreatedItems(model, id);
        return "users_show";
    }

    @GetMapping("/show")
    public String showUser(Principal principal, Model model) {
        this.checkUser(principal);
        this.addAllCreatedItems(model, this.user.getId());
        return "users_show";
    }

    private void addAllCreatedItems(Model model, Long id) {
        model.addAttribute("user", userService.show(id));
        model.addAttribute("documents", documentService.findByCreator(id));
        model.addAttribute("fasteners", fastenerService.findByCreator(id));
        model.addAttribute("manufacturers", manufacturerService.findByCreator(id));
        model.addAttribute("consumables", consumableService.findByCreator(id));
        model.addAttribute("tools", toolService.findByCreator(id));
        model.addAttribute("parts", partService.findByCreator(id));
        model.addAttribute("bikes", bikeService.findByCreator(id));
    }

    @PostMapping("/update/{id}")
    @Secured("ROLE_ADMIN")
    public String update(@PathVariable("id") Long id,
                         @RequestParam(value = "admin") int role) {
        switch (role) {
            case 1 -> userService.userToAdmin(id);
            case 0 -> userService.adminToUser(id);
        }
        return "redirect:/users/{id}";
    }

    @PostMapping("/delete/{id}")
    @Secured("ROLE_ADMIN")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/users_index";
    }

    @Autowired
    private void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Autowired
    private void setFastenerService(FastenerService fastenerService) {
        this.fastenerService = fastenerService;
    }

    @Autowired
    private void setManufacturerService(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @Autowired
    private void setConsumableService(ConsumableService consumableService) {
        this.consumableService = consumableService;
    }

    @Autowired
    private void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }

    @Autowired
    private void setPartService(PartService partService) {
        this.partService = partService;
    }

    @Autowired
    private void setBikeService(BikeService bikeService) {
        this.bikeService = bikeService;
    }
}
