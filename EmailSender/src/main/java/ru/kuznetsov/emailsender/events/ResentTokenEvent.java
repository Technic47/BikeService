package ru.kuznetsov.emailsender.events;

import org.springframework.context.ApplicationEvent;
import ru.kuznetsov.emailsender.utils.ByteUtils;

import java.util.Map;

public class ResentTokenEvent extends ApplicationEvent {
    private final String appUrl;
    private final String userEmail;
    private final String token;

    public ResentTokenEvent(String appUrl, String userEmail, String token) {
        super(userEmail);
        this.appUrl = appUrl;
        this.userEmail = userEmail;
        this.token = token;
    }

    public static ResentTokenEvent getFromBytes(byte[] eventBytes) {
        Map<String, String> fields = (Map<String, String>) ByteUtils.fromBytes(eventBytes);
        return new ResentTokenEvent(
                fields.get("appUrl"),
                fields.get("userEmail"),
                fields.get("token")
        );
    }

    public String getAppUrl() {
        return appUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getToken() {
        return token;
    }
}
