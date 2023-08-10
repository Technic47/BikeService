package ru.kuznetsov.bikeService.eventListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.security.OnRegistrationCompleteEvent;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.EmailService;

import static ru.kuznetsov.bikeService.config.SpringConfig.BACK_LINK;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {
    private final EmailService emailService;

    @Autowired
    public RegistrationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        UserModel user = event.getUser();
        emailService.constructSendVerificationEmail(user, BACK_LINK);
    }
}
