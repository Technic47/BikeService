package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.config.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * Service for sending verification emails.
 */
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final VerificationTokenService tokenService;
    @Value("${smtp.mail.fromWho}")
    private String smtpFrom;

    public EmailService(JavaMailSender mailSender, VerificationTokenService tokenService, @Qualifier("MainExecutor") ExecutorService mainExecutor) {
        this.mailSender = mailSender;
        this.tokenService = tokenService;
    }

    /**
     * Construct verification mail with token for new user
     *
     * @param user        new userModel for verification
     * @param contextPath path for return link
     */
    public void constructSendVerificationEmail(UserModel user, String contextPath) {
        String token = UUID.randomUUID().toString();
        tokenService.createVerificationToken(user, token);

        String confirmationUrl = "/registrationConfirm?token=" + token;
        String message = "Для окончания регистрации пройдите по следующей ссылке:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setFrom(smtpFrom);
        email.setSubject("Подтверждение регистрации на yourbikeservice.");
        email.setText(message + "\r\n" + contextPath + confirmationUrl);
        mailSender.send(email);
    }

    /**
     * Construct verification mail with updated token for existing user
     *
     * @param userEmail   email for verification
     * @param newToken    token for link
     * @param contextPath path for return link
     */
    public void constructResendVerificationTokenEmail(String userEmail, VerificationToken newToken, String contextPath) {
        String confirmationUrl = "/registrationConfirm?token=" + newToken.getToken();
        String message = "Для регистрации пройдите по следующей ссылке:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setFrom(smtpFrom);
        email.setSubject("Resend Registration Token");
        email.setText(message + "\r\n" + contextPath + confirmationUrl);
        mailSender.send(email);
    }
}
