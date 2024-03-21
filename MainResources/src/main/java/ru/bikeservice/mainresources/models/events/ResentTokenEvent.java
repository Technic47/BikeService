package ru.bikeservice.mainresources.models.events;

import org.springframework.context.ApplicationEvent;
import ru.bikeservice.mainresources.models.security.VerificationToken;
import ru.bikeservice.mainresources.utils.ByteUtils;

import java.util.HashMap;
import java.util.Map;

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

    public byte[] getBytes(){
        Map<String, String> fields = new HashMap<>();
        fields.put("appUrl", appUrl);
        fields.put("userEmail", userEmail);
        fields.put("token", token.getToken());
        return ByteUtils.toBytes(fields);
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
