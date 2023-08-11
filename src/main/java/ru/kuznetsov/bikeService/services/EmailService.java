package ru.kuznetsov.bikeService.services;

import com.sun.mail.smtp.SMTPTransport;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final VerificationTokenService tokenService;
    private final ExecutorService mainExecutor;
    private final Session session;

    @Value("${smtp.mail.host}")
    private String smtpHost;
    @Value("${smtp.mail.port}")
    private String smtpPort;
    @Value("${smtp.mail.username}")
    private String smtpUserName;
    @Value("${smtp.mail.password}")
    private String smtpUserPass;

    public EmailService(JavaMailSender mailSender, VerificationTokenService tokenService, @Qualifier("MainExecutor") ExecutorService mainExecutor, Session session) {
        this.mailSender = mailSender;
        this.tokenService = tokenService;
        this.mainExecutor = mainExecutor;
        this.session = session;
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

            Message email = new MimeMessage(session);
            try {
                email.setFrom(new InternetAddress("postmaster@sandbox5564fb0492284e5d93da11c3475e5238.mailgun.org"));
                InternetAddress[] addrs = InternetAddress.parse(user.getEmail(), false);
                email.setRecipients(Message.RecipientType.TO, addrs);

                email.setSubject("Подтверждение регистрации на yourbikeservice.");
                email.setText(message + "\r\n" + contextPath + confirmationUrl);
                email.setSentDate(new Date());
                SMTPTransport t =
                        (SMTPTransport) session.getTransport("smtps");
                t.connect(smtpHost,
                        smtpUserName,
                        smtpUserPass);
                t.sendMessage(email, email.getAllRecipients());

//                System.out.println("Response: " + t.getLastServerResponse());

                t.close();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
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

            Message email = new MimeMessage(session);
            try {
                email.setFrom(new InternetAddress("postmaster@sandbox5564fb0492284e5d93da11c3475e5238.mailgun.org"));
                InternetAddress[] addrs = InternetAddress.parse(user.getEmail(), false);
                email.setRecipients(Message.RecipientType.TO, addrs);
                email.setSubject("Повторная отправка регистрации на yourbikeservice.");
                email.setText(message + "\r\n" + contextPath + confirmationUrl);
                email.setSentDate(new Date());

                SMTPTransport t =
                        (SMTPTransport) session.getTransport("smtps");
                t.connect(smtpHost,
                        smtpUserName,
                        smtpUserPass);
                t.sendMessage(email, email.getAllRecipients());

//                System.out.println("Response: " + t.getLastServerResponse());

                t.close();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        };
        mainExecutor.submit(emailSend);
    }
}
