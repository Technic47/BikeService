package ru.kuznetsov.bikeService.controllers.notRest.additional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.events.OnRegistrationCompleteEvent;
import ru.kuznetsov.bikeService.models.events.ResentTokenEvent;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.VerificationTokenService;

import java.security.Principal;


@Controller
@RequestMapping("/")
public class AuthController extends AbstractController {
    private ApplicationEventPublisher eventPublisher;
    private VerificationTokenService tokenService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String root() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/successLogin")
    public String confirmLogin(Model model, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        this.addUserToModel(model, principal);
        logger.info(userModel.getEmail() + " logged in");
        return "title";
    }

    @GetMapping("/title")
    public String index(Model model, Principal principal) {
        this.addUserToModel(model, principal);
        return "title";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserModel());
        return "registration";
    }


    @PostMapping("/registration")
    public String registerUserAccount(@Valid UserModel userModel,
                                      BindingResult bindingResult,
                                      HttpServletRequest request,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(userModel);
            return "registration";
        }
        try {
            UserModel registered = userService.registerNewUserAccount(userModel);
            String appUrl =
                    "https://" + request.getServerName() +
                            ":" + request.getServerPort() +
                            request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
        } catch (RuntimeException ex) {
            model.addAttribute("user", userModel);
            model.addAttribute("message", ex.getMessage());
            return "registration";
        }

        String message = "На почту " + userModel.getEmail() + " отправлено сообщение с кодом активации.";
        model.addAttribute("user", userModel);
        model.addAttribute("regMessage", message);
        model.addAttribute("regStart", true);
        return "registration";
    }

    @GetMapping("/info")
    public String info(Model model, Principal principal) {
        this.addUserToModel(model, principal);
        return "info";
    }

    @PostMapping("/updateEmail")
    public String updateEmail
            (@RequestParam(value = "login") String login,
             @RequestParam(value = "email") String email,
             HttpServletRequest request,
             Model model) {
        UserModel userModel = userService.findByUsernameOrNull(login);
        if (userModel == null) {
            String message = "Не найден пользователь с логином: " + login;
            model.addAttribute("regMessage", message);
            return "login";
        } else {
            if (userModel.getEmail() != null) {
                String message = "У пользователя с логином " + login + " уже задана почта!";
                model.addAttribute("regMessage", message);
                return "login";
            }
            userModel.setEmail(email);
            userService.save(userModel);
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userModel, appUrl));
            String message = "Письмо отправлено на адрес: " + email;
            model.addAttribute("regMessage", message);
        }
        return "login";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration
            (Model model, @RequestParam(value = "token") String token) {
        VerificationToken verificationToken = tokenService.findAndCheckToken(token);
        UserModel user = verificationToken.getUser();
        user.setEnabled(true);
        userService.save(user);
        model.addAttribute("user", user);
        return "redirect:/login";
    }

    @GetMapping("/resendRegistrationToken")
    public String resendRegistrationToken(HttpServletRequest request,
                                          @RequestParam(value = "email") String email,
                                          @RequestParam(value = "returnLink") String returnLink,
                                          Model model) {
        UserModel userModel = userService.findByEmailOrNull(email);
        String message = "Повторное письмо отправлено!";

        if (userModel == null) {
            message = "Проверьте правильность написания почты.";
            model.addAttribute("user", new UserModel());
        } else {
            VerificationToken token = tokenService.createOrUpdateToken(userModel);
            String appUrl =
                    "https://" + request.getServerName() +
                            ":" + request.getServerPort() +
                            request.getContextPath();
            eventPublisher.publishEvent(new ResentTokenEvent(appUrl, userModel.getEmail(), token));
            model.addAttribute("user", userModel);
        }

        model.addAttribute("regMessage", message);
        return returnLink;
    }

    @Autowired
    private void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    private void setTokenService(VerificationTokenService tokenService) {
        this.tokenService = tokenService;
    }
}
