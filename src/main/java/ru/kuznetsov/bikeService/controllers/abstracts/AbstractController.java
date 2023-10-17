package ru.kuznetsov.bikeService.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.bikeservice.mainresources.services.PictureService;
import ru.bikeservice.mainresources.services.modelServices.*;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.UserService;

import java.security.Principal;
import java.util.concurrent.ExecutorService;

/**
 * Abstract service that includes main dependencies.
 */
@Component
public abstract class AbstractController {
    public final static Logger logger = LoggerFactory.getLogger("BikeServiceLogger");
    protected UserService userService;
    protected PictureService pictureService;
    protected DocumentService documentService;
    protected FastenerService fastenerService;
    protected ManufacturerService manufacturerService;
    protected ConsumableService consumableService;
    protected ToolService toolService;
    protected PartService partService;
    protected BikeService bikeService;
    protected ExecutorService mainExecutor;
    protected ExecutorService additionExecutor;

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

    @Autowired
    private void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Autowired
    private void setFastenerService(FastenerService fastenerService) {
        this.fastenerService = fastenerService;
    }

    @Autowired
    private void setManufacturerService(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @Autowired
    private void setConsumableService(ConsumableService consumableService) {
        this.consumableService = consumableService;
    }

    @Autowired
    private void setToolService(ToolService toolService) {
        this.toolService = toolService;
    }

    @Autowired
    private void setPartService(PartService partService) {
        this.partService = partService;
    }

    @Autowired
    private void setBikeService(BikeService bikeService) {
        this.bikeService = bikeService;
    }
}
