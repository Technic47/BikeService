package ru.kuznetsov.bikeService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController extends AbstractController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login(Principal principal) {
        logger.info(principal.getName() + " logged in");
        return "title";
    }

    @GetMapping("/")
    public String root() {
        return "home";
    }

    @GetMapping("/title")
    public String index() {
        return "title";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(UserModel user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("message", "Current user name '" + user.getUsername() + "' is occupied");
            return "/registration";
        }
        userService.createUser(user);
        logger.debug(user.getUsername() + " " + user.getStatus() + " registered");
        return "redirect:/login";
    }
}
