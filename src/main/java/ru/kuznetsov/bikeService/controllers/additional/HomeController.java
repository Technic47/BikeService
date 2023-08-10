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
import ru.kuznetsov.bikeService.services.EmailService;
import ru.kuznetsov.bikeService.services.VerificationTokenService;

import java.security.Principal;

import static ru.kuznetsov.bikeService.config.SpringConfig.BACK_LINK;


@Controller
@RequestMapping("/")
public class HomeController extends AbstractController {
    private ApplicationEventPublisher eventPublisher;
    private VerificationTokenService tokenService;
    private EmailService emailService;

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
            VerificationToken newToken = tokenService.updateVerificationToken(userModel);
            UserModel user = tokenService.findUserByTokenString(newToken.getToken());
//        String appUrl =
//                "http://" + request.getServerName() +
//                        ":" + request.getServerPort() +
//                        request.getContextPath();
            emailService.constructResendVerificationTokenEmail(user, newToken, BACK_LINK);
            model.addAttribute("user", user);
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

    @Autowired
    private void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
