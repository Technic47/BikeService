package ru.kuznetsov.bikeService.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;
import ru.kuznetsov.bikeService.services.modelServices.BikeService;
import ru.kuznetsov.bikeService.services.modelServices.PartService;

import java.security.Principal;
import java.util.concurrent.ExecutorService;

@Component
public abstract class AbstractController {
    public final static Logger logger = LoggerFactory.getLogger("BikeServiceLogger");
    protected UserService userService;
    protected PictureService pictureService;
    protected PartService partService;
    protected BikeService bikeService;
    protected ExecutorService mainExecutor;
    protected ExecutorService additionExecutor;

    protected UserModel getUserModelFromPrincipal(Principal principal) {
        String userName;
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            userName = principal.getName();
        } else if (principal instanceof OAuth2AuthenticationToken) {
            userName = ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email");
        } else userName = null;
        return this.userService.findByName(userName);
    }

    protected void addUserToModel(Model model, Principal principal) {
        model.addAttribute("user", this.getUserModelFromPrincipal(principal));
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPictureService(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @Autowired
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    @Autowired
    public void setBikeService(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @Autowired
    @Qualifier("MainExecutor")
    public void setMainExecutor(ExecutorService mainExecutor) {
        this.mainExecutor = mainExecutor;
    }

    @Autowired
    @Qualifier("AdditionExecutor")
    public void setAdditionExecutor(ExecutorService additionExecutor) {
        this.additionExecutor = additionExecutor;
    }
}
