package ru.kuznetsov.bikeService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.security.OnRegistrationCompleteEvent;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.UserService;

import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {
    private final UserService service;
    private final JavaMailSender mailSender;

    @Autowired
    public RegistrationListener(UserService service, JavaMailSender mailSender) {
        this.service = service;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        UserModel user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = "Для окончания регистрации пройдите по следующей ссылке:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setFrom("yourbikeservice.verification@yandex.ru");
        email.setSubject("Подтверждение регистрации на yourbikeservice.");
        email.setText(message + "\r\n" + "http://localhost:8888" + confirmationUrl);
        mailSender.send(email);
    }
}
