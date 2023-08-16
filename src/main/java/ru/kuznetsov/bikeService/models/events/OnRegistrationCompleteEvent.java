package ru.kuznetsov.bikeService.models.events;

import org.springframework.context.ApplicationEvent;
import ru.kuznetsov.bikeService.models.users.UserModel;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private UserModel userModel;

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
