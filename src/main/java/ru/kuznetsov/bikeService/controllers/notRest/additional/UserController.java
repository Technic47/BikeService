package ru.kuznetsov.bikeService.controllers.notRest.additional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.VerificationTokenService;
import ru.kuznetsov.bikeService.services.modelServices.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/users")
public class UserController extends AbstractController {
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenService tokenService;

    public UserController(DocumentService documentService, FastenerService fastenerService, ManufacturerService manufacturerService, ConsumableService consumableService, ToolService toolService, PartService partService, BikeService bikeService, ApplicationEventPublisher eventPublisher, VerificationTokenService tokenService) {
        this.documentService = documentService;
        this.fastenerService = fastenerService;
        this.manufacturerService = manufacturerService;
        this.consumableService = consumableService;
        this.toolService = toolService;
        this.partService = partService;
        this.bikeService = bikeService;
        this.eventPublisher = eventPublisher;
        this.tokenService = tokenService;
    }


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
        model.addAttribute("userAccount", userService.getById(id));
        model.addAttribute("documents", documentService.findByCreator(id));
        model.addAttribute("fasteners", fastenerService.findByCreator(id));
        model.addAttribute("manufacturers", manufacturerService.findByCreator(id));
        model.addAttribute("consumables", consumableService.findByCreator(id));
        model.addAttribute("tools", toolService.findByCreator(id));
        model.addAttribute("parts", partService.findByCreator(id));
        model.addAttribute("bikes", bikeService.findByCreator(id));

//        Callable<List<Document>> getDocuments = () -> documentService.findByCreator(id);
//        Callable<List<Fastener>> getFasteners = () -> fastenerService.findByCreator(id);
//        Callable<List<Manufacturer>> getManufacturers = () -> manufacturerService.findByCreator(id);
//        Callable<List<Consumable>> getConsumables = () -> consumableService.findByCreator(id);
//        Callable<List<Tool>> getTools = () -> toolService.findByCreator(id);
//        Callable<List<Part>> getParts = () -> partService.findByCreator(id);
//        Callable<List<Bike>> getBikes = () -> bikeService.findByCreator(id);
//        try {
//            model.addAttribute("documents", mainExecutor.submit(getDocuments).get());
//            model.addAttribute("fasteners", mainExecutor.submit(getFasteners).get());
//            model.addAttribute("manufacturers", mainExecutor.submit(getManufacturers).get());
//            model.addAttribute("consumables", mainExecutor.submit(getConsumables).get());
//            model.addAttribute("tools", mainExecutor.submit(getTools).get());
//            model.addAttribute("parts", mainExecutor.submit(getParts).get());
//            model.addAttribute("bikes", mainExecutor.submit(getBikes).get());
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
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
        return "credentialsChange";
    }

    @PostMapping("/update/credentialsChange")
    public String uploadNewCredentials(@Valid @ModelAttribute("user") UserModel userModelNew,
                                       BindingResult bindingResult,
                                       HttpServletRequest request,
                                       Model model,
                                       Principal principal) {
        if (bindingResult.hasErrors()) {
            this.addUserToModel(model, principal);
            return "credentialsChange";
        }
        if (userService.findByEmailOrNull(userModelNew.getEmail()) != null) {
            model.addAttribute("emailWrong", "Данная почта уже зарегистрирована в системе!");
            this.addUserToModel(model, principal);
            return "credentialsChange";
        }
        UserModel userModelExist = this.getUserModelFromPrincipal(principal);
        userModelExist.setEnabled(false);
        this.userService.update(userModelExist, userModelNew);

        tokenService.createUpdateTokenEvent(request, userModelExist);
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
}
