package ru.kuznetsov.bikeService.models.events;

import org.springframework.context.ApplicationEvent;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.kuznetsov.bikeService.utils.ByteUtils;

import java.util.HashMap;
import java.util.Map;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private UserModel userModel;

    public byte[] getBytes() {
        Map<String, String> fields = new HashMap<>();
        fields.put("appUrl", appUrl);
        fields.put("userEmail", userModel.getEmail());
        return ByteUtils.toBytes(fields);
    }

    public OnRegistrationCompleteEvent(UserModel user, String appUrl) {
        super(user);
        this.userModel = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }


    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
