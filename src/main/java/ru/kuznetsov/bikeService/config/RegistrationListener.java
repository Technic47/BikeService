package ru.kuznetsov.bikeService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.security.OnRegistrationCompleteEvent;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.UserService;

import static ru.kuznetsov.bikeService.config.SpringConfig.BACK_LINK;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {
    private final UserService service;

    @Autowired
    public RegistrationListener(UserService service, JavaMailSender mailSender) {
        this.service = service;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        UserModel user = event.getUser();
        service.constructSendVerificationEmail(user, BACK_LINK);
    }
}
