package ru.kuznetsov.bikeService.models.events;

import org.springframework.context.ApplicationEvent;
import ru.kuznetsov.bikeService.models.security.VerificationToken;

public class ResentTokenEvent extends ApplicationEvent {
    private final String appUrl;
    private final String userEmail;
    private final VerificationToken token;

    public ResentTokenEvent(String appUrl, String userEmail, VerificationToken token) {
        super(userEmail);
        this.appUrl = appUrl;
        this.userEmail = userEmail;
        this.token = token;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public VerificationToken getToken() {
        return token;
    }
}
