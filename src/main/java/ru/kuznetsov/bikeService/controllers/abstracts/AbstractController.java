package ru.kuznetsov.bikeService.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
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

    protected void updateUser(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            this.update(((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email").toString());
        } else if (principal instanceof UsernamePasswordAuthenticationToken){
            this.update(principal.getName());
        }
    }

    private void update(String userName){
        if (this.user == null) {
            this.user = userService.findByName(userName);
        } else {
            if (!Objects.equals(this.user.getUsername(), userName)) {
                this.user = userService.findByName(userName);
            }
        }
    }

    protected void addUserToModel(Model model, Principal principal) {
        this.updateUser(principal);
        model.addAttribute("user", this.user);
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
