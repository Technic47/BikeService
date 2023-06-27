package ru.kuznetsov.bikeService.controllers.additional;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController extends AbstractController {
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String root() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        this.addUserToModel(model, principal);
        logger.info(principal.getName() + " logged in");
        return "title";
    }

    @PostMapping("/login")
    public String postLogin() {
        return "title";
    }

    @GetMapping("/successLogin")
    public String confirmLogin(Model model, OAuth2AuthenticationToken authentication) {
        this.addUserToModel(model, authentication);
        logger.info(authentication.getPrincipal().getAttribute("email") + " logged in");
        return "title";
    }

    @GetMapping("/title")
    public String index(Model model, Principal principal) {
        this.addUserToModel(model, principal);
        return "title";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(UserModel user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("message", "Имя '" + user.getUsername() + "' уже занято!");
            return "/registration";
        }
        userService.createUser(user);
        logger.debug(user.getUsername() + " " + user.getAuthorities() + " registered");
        return "redirect:/login";
    }

    @GetMapping("/info")
    public String info(Model model, Principal principal) {
        this.addUserToModel(model, principal);
        return "info";
    }
}
