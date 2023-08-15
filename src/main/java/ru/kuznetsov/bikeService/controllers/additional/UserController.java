package ru.kuznetsov.bikeService.controllers.additional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.modelServices.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/users")
public class UserController extends AbstractController {
    private DocumentService documentService;
    private FastenerService fastenerService;
    private ManufacturerService manufacturerService;
    private ConsumableService consumableService;
    private ToolService toolService;


    @GetMapping()
    @Secured("ROLE_ADMIN")
    public String index(Model model, Principal principal) {
        this.addUserToModel(model, principal);
        model.addAttribute("users", userService.index());
        return "users_index";
    }


    @GetMapping("/{id}")
    @Secured("ROLE_ADMIN")
    public String showToAdmin(@PathVariable("id") Long id,
                              Model model,
                              Principal principal) {
        this.addUserToModel(model, principal);
        this.addAllCreatedItems(model, id);
        model.addAttribute("owner", false);
        return "users_show";
    }

    @GetMapping("/show")
    public String showToOwner(Model model, Principal principal) {
        this.addUserToModel(model, principal);
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        this.addAllCreatedItems(model, userModel.getId());
        model.addAttribute("owner", true);
        return "users_show";
    }

    private void addAllCreatedItems(Model model, Long id) {
        model.addAttribute("userAccount", userService.show(id));
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

    @GetMapping("/update")
    public String updateNameOrPassword(Model model, Principal principal) {
        this.addUserToModel(model, principal);
        return "namePassChange";
    }

    @PostMapping("/update/credentialsChange")
    public String uploadNewCredentials(@Valid @ModelAttribute("user") UserModel userModelNew,
                                       BindingResult bindingResult,
                                       Model model,
                                       Principal principal) {
        if (bindingResult.hasErrors()) {
            this.addUserToModel(model, principal);
            return "namePassChange";
        }
        UserModel userModelExist = this.getUserModelFromPrincipal(principal);
        this.userService.update(userModelExist, userModelNew);
        return "redirect:/logout";
    }

    @PostMapping("/delete/{id}")
    @Secured("ROLE_ADMIN")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/users_index";
    }

    @GetMapping("/search")
    @Secured("ROLE_ADMIN")
    public String search(@RequestParam(value = "value") String value,
                         Model model,
                         Principal principal) {
        Set<UserModel> resultSet = new HashSet<>(this.userService.findByUsernameContainingIgnoreCase(value));
        this.addUserToModel(model, principal);
        model.addAttribute("users", resultSet);
        return "users_index";
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
}
