package ru.kuznetsov.bikeService.controllers.rest.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.dto.RegistrationRequestDto;
import ru.kuznetsov.bikeService.models.dto.UserDto;
import ru.kuznetsov.bikeService.models.events.OnRegistrationCompleteEvent;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.VerificationTokenService;

@RestController
@RequestMapping("/api/registration")
public class RegistrationControllerREST extends AbstractController {
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenService tokenService;

    public RegistrationControllerREST(ApplicationEventPublisher eventPublisher, VerificationTokenService tokenService) {
        this.eventPublisher = eventPublisher;
        this.tokenService = tokenService;
    }

    @PostMapping()
    public UserDto newUser(@Valid @RequestBody RegistrationRequestDto dto,
                           HttpServletRequest request
    ) {

        UserModel registered = userService.registerNewUserAccount(dto);
        String appUrl =
                "https://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
        return new UserDto(registered);
    }

    @GetMapping("/confirm")
    public void confirm(@RequestParam(value = "token") String token) {
        VerificationToken verificationToken = tokenService.findAndCheckToken(token);
        UserModel user = verificationToken.getUser();
        user.setEnabled(true);
        userService.save(user);
    }

    @GetMapping("/resendToken")
    public void resendToken(HttpServletRequest request,
                            @RequestParam(value = "email") String email) {
        UserModel userModel = userService.findByEmailOrNull(email);

        tokenService.createUpdateTokenEvent(request, userModel);
    }
}
