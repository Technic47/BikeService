package ru.bikeservice.mainresources.controllers.abstracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.bikeservice.mainresources.services.PictureService;
import ru.bikeservice.mainresources.services.UserService;
import ru.bikeservice.mainresources.services.modelServices.*;

import java.security.Principal;
import java.util.concurrent.ExecutorService;

/**
 * Abstract service that includes main dependencies.
 */
@Component
public abstract class AbstractController {
    @Autowired
    protected UserService userService;
    @Autowired
    protected PictureService pictureService;
    @Autowired
    protected DocumentService documentService;
    @Autowired
    protected FastenerService fastenerService;
    @Autowired
    protected ManufacturerService manufacturerService;
    @Autowired
    protected ConsumableService consumableService;
    @Autowired
    protected ToolService toolService;
    @Autowired
    protected PartService partService;
    @Autowired
    protected BikeService bikeService;
    @Autowired
    @Qualifier("MainExecutor")
    protected ExecutorService mainExecutor;
    @Autowired
    @Qualifier("AdditionExecutor")
    protected ExecutorService additionExecutor;

    protected AbstractController() {
    }

    protected UserModel getUserModelFromPrincipal(Principal principal) {
        String userName;
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            userName = principal.getName();
            return this.userService.findByUsernameOrNull(userName);
        } else if (principal instanceof OAuth2AuthenticationToken) {
            userName = ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email");
            return this.userService.findByEmailOrNull(userName);
        } else return null;
    }

    protected void addUserToModel(Model model, Principal principal) {
        model.addAttribute("user", this.getUserModelFromPrincipal(principal));
    }

    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private void setPictureService(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @Autowired
    @Qualifier("MainExecutor")
    private void setMainExecutor(ExecutorService mainExecutor) {
        this.mainExecutor = mainExecutor;
    }

    @Autowired
    @Qualifier("AdditionExecutor")
    private void setAdditionExecutor(ExecutorService additionExecutor) {
        this.additionExecutor = additionExecutor;
    }
}
