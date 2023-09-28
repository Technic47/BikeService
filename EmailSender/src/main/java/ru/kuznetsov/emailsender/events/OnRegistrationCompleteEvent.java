package ru.kuznetsov.emailsender.events;

import org.springframework.context.ApplicationEvent;
import ru.kuznetsov.emailsender.utils.ByteUtils;

import java.util.Map;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private String userEmail;

    public OnRegistrationCompleteEvent(String userEmail, String appUrl) {
        super(userEmail);
        this.userEmail = userEmail;
        this.appUrl = appUrl;
    }

    public static OnRegistrationCompleteEvent getFromBytes(byte[] eventBytes) {
        Map<String, String> fields = (Map<String, String>) ByteUtils.fromBytes(eventBytes);
        return new OnRegistrationCompleteEvent(fields.get("userEmail"), fields.get("appUrl"));
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
