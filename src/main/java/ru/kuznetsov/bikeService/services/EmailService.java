package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final VerificationTokenService tokenService;
    private final ExecutorService mainExecutor;

    public EmailService(JavaMailSender mailSender, VerificationTokenService tokenService, @Qualifier("MainExecutor") ExecutorService mainExecutor) {
        this.mailSender = mailSender;
        this.tokenService = tokenService;
        this.mainExecutor = mainExecutor;
    }

    /**
     * Construct verification mail with token for new user
     *
     * @param user        new userModel for verification
     * @param contextPath path for return link
     */
    public void constructSendVerificationEmail(UserModel user, String contextPath) {
        Runnable emailSend = () -> {
            String token = UUID.randomUUID().toString();
            tokenService.createVerificationToken(user, token);

            String confirmationUrl = "/registrationConfirm?token=" + token;
            String message = "Для окончания регистрации пройдите по следующей ссылке:";

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(user.getEmail());
            email.setFrom("yourbikeservice.verification@yandex.ru");
            email.setSubject("Подтверждение регистрации на yourbikeservice.");
            email.setText(message + "\r\n" + contextPath + confirmationUrl);
            mailSender.send(email);
        };
        mainExecutor.submit(emailSend);
    }

    /**
     * Construct verification mail with updated token for existing user
     *
     * @param user        userModel for verification
     * @param contextPath path for return link
     */
    public void constructResendVerificationTokenEmail(UserModel user, VerificationToken newToken, String contextPath) {
        Runnable emailSend = () -> {
            String confirmationUrl = "/registrationConfirm?token=" + newToken.getToken();
            String message = "Для регистрации пройдите по следующей ссылке:";

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(user.getEmail());
            email.setFrom("yourbikeservice.verification@yandex.ru");
            email.setSubject("Resend Registration Token");
            email.setText(message + "\r\n" + contextPath + confirmationUrl);
            mailSender.send(email);
        };
        mainExecutor.submit(emailSend);
    }
}
