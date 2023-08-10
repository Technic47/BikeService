package ru.kuznetsov.bikeService.models.security;

import org.springframework.context.ApplicationEvent;
import ru.kuznetsov.bikeService.models.users.UserModel;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private UserModel user;

    public OnRegistrationCompleteEvent(UserModel user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }


    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
