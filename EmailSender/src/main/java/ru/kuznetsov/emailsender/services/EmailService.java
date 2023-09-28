package ru.kuznetsov.emailsender.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kuznetsov.emailsender.events.OnRegistrationCompleteEvent;
import ru.kuznetsov.emailsender.events.ResentTokenEvent;

import java.util.UUID;

/**
 * Service for sending verification emails.
 */
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${smtp.mail.fromWho}")
    private String smtpFrom;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Construct verification mail with token for new user
     */
    @KafkaListener(topics = "emailRegistration", groupId = "emailSender")
    public void constructSendVerificationEmail(byte[] eventBytes) {
        OnRegistrationCompleteEvent event = OnRegistrationCompleteEvent.getFromBytes(eventBytes);

        String userEmail = event.getUserEmail();
        String contextPath = event.getAppUrl();
        String token = UUID.randomUUID().toString();

        String confirmationUrl = "/registrationConfirm?token=" + token;
        String message = "Для окончания регистрации пройдите по следующей ссылке:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setFrom(smtpFrom);
        email.setSubject("Подтверждение регистрации на yourbikeservice.");
        email.setText(message + "\r\n" + contextPath + confirmationUrl);

        mailSender.send(email);
    }

    /**
     * Construct verification mail with updated token for existing user
     */
    @KafkaListener(topics = "emailResend", groupId = "emailSender")
    public void constructResendVerificationTokenEmail(byte[] eventBytes) {
        ResentTokenEvent event = ResentTokenEvent.getFromBytes(eventBytes);
        String userEmail = event.getUserEmail();
        String newToken = event.getToken();
        String contextPath = event.getAppUrl();

        String confirmationUrl = "/registrationConfirm?token=" + newToken;
        String message = "Для регистрации пройдите по следующей ссылке:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setFrom(smtpFrom);
        email.setSubject("Resend Registration Token");
        email.setText(message + "\r\n" + contextPath + confirmationUrl);
        mailSender.send(email);
    }
}
