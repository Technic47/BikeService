package ru.kuznetsov.bikeService.controllers.additional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.security.GenericResponse;
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
    public String login(Model model, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        this.addUserToModel(model, principal);
        logger.info(userModel.getName() + " logged in");
        return "title";
    }

    @PostMapping("/login")
    public String postLogin() {
        return "title";
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
    public String registration() {
        return "registration";
    }

//    @PostMapping("/registration")
//    public String createUser(UserModel user, Model model) {
//        if (!userService.createUser(user)) {
//            model.addAttribute("message", "Имя '" + user.getUsername() + "' уже занято!");
//            return "/registration";
//        }
//        userService.createUser(user);
//        logger.debug(user.getUsername() + " " + user.getAuthorities() + " registered");
//        return "redirect:/login";
//    }

    @PostMapping("/registration")
    public String registerUserAccount(@Valid UserModel userModel,
                                      HttpServletRequest request) {

        try {
            UserModel registered = userService.registerNewUserAccount(userModel);

            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered,
                    request.getLocale(), appUrl));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        return "redirect:/title";
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
    public GenericResponse resendRegistrationToken(HttpServletRequest request,
                                                   @RequestParam("token") String existingToken) {
        VerificationToken newToken = userService.generateNewVerificationToken(existingToken);

        UserModel user = userService.findByToken(newToken.getToken());
        String appUrl =
                "http://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();
        userService.constructResendVerificationTokenEmail(user, newToken, appUrl);

        return new GenericResponse("message.resendToken");
    }

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
