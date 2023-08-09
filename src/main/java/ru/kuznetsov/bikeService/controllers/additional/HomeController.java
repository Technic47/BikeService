package ru.kuznetsov.bikeService.controllers.additional;

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
import ru.kuznetsov.bikeService.models.security.OnRegistrationCompleteEvent;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.security.Principal;
import java.util.Calendar;


@Controller
@RequestMapping("/")
public class HomeController extends AbstractController {

    private ApplicationEventPublisher eventPublisher;

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
        logger.info(userModel.getAttribute("email") + " logged in");
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

            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
                    request.getLocale(), appUrl));
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

    @GetMapping("/registrationConfirm")
    public String confirmRegistration
            (Model model, @RequestParam("token") String token) {

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = "invalidToken";
            model.addAttribute("message", message);
            return "redirect:/regError";
        }

        UserModel user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = "token expired";
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
            model.addAttribute("message", messageValue);
            return "redirect:/regError";
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return "redirect:/login";
    }

    @GetMapping("/resendRegistrationToken")
    public String resendRegistrationToken(HttpServletRequest request,
                                          UserModel userModel,
                                          Model model) {
        VerificationToken newToken = userService.generateNewVerificationToken(userModel);

        UserModel user = userService.findByToken(newToken.getToken());
        String appUrl =
                "http://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();
        userService.constructResendVerificationTokenEmail(user, newToken, appUrl);
        model.addAttribute("user", user);
        return "registration";
    }

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
