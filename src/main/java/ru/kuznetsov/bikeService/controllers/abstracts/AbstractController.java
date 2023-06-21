package ru.kuznetsov.bikeService.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;

import java.security.Principal;
import java.util.Objects;

@Component
public abstract class AbstractController {
    public final static Logger logger = LoggerFactory.getLogger("BikeServiceLogger");
    protected UserService userService;
    protected PictureService pictureService;

    protected UserModel user;

    protected void checkUser(Principal principal) {
        if (this.user == null) {
            this.user = userService.findByName(principal.getName());
        } else {
            if (!Objects.equals(this.user.getUsername(), principal.getName())) {
                this.user = userService.findByName(principal.getName());
            }
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPictureService(PictureService pictureService) {
        this.pictureService = pictureService;
    }
}
