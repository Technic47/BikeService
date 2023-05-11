package ru.kuznetsov.bikeService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.services.*;

import java.security.Principal;
import java.util.List;

@Controller
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
    public String index(Principal principal,
                        Model model) {
        model.addAttribute("users", userService.index());
        return "users_index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Principal principal,
                       Model model) {
        List<Document> documentList = documentService.findByCreator(id);
        List<Fastener> fastenerList = fastenerService.findByCreator(id);
        List<Manufacturer> manufacturerList = manufacturerService.findByCreator(id);
        List<Consumable> consumableList = consumableService.findByCreator(id);
        List<Tool> toolList = toolService.findByCreator(id);
        List<Part> partList = partService.findByCreator(id);
        List<Bike> bikeList = bikeService.findByCreator(id);
        model.addAttribute("user", userService.show(id));
        model.addAttribute("documents", documentList);
        model.addAttribute("fasteners", fastenerList);
        model.addAttribute("manufacturers", manufacturerList);
        model.addAttribute("comsumables", consumableList);
        model.addAttribute("tools", toolList);
        model.addAttribute("parts", partList);
        model.addAttribute("bikes", bikeList);
        return "users_show";
    }

//    @GetMapping(value = "/new")
//    public String newItem(Model model) {
//        model.addAttribute("newUser", (new UserModel()));
//    }
//
//    @PostMapping()
//    public String create(@Valid @ModelAttribute("object") UserModel user,
//                         BindingResult bindingResult,
//                         @RequestParam(value = "role") String role,
//                         Principal principal,
//                         Model model) {
//        if (bindingResult.hasErrors()) {
//
//        }
//        switch (role) {
//            case "user" -> userService.createUser(user);
//            case "admin" -> userService.createAdmin(user);
//        }
//        logger.info("User " + user.getUsername() + "was created by " + principal.getName());
//    }

    @PostMapping("/{id}")
    public String delete(@PathVariable("id") Long id,
                         Principal principal) {
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
