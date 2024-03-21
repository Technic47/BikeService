package ru.bikeservice.mainresources.eventListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import ru.bikeservice.mainresources.customExceptions.ResourceNotFoundException;
import ru.bikeservice.mainresources.models.events.OnRegistrationCompleteEvent;
import ru.bikeservice.mainresources.models.events.ResentTokenEvent;
import ru.bikeservice.mainresources.models.pictures.Picture;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.bikeservice.mainresources.services.PictureService;
import ru.bikeservice.mainresources.services.UserService;
import ru.bikeservice.mainresources.services.VerificationTokenService;
import ru.bikeservice.mainresources.services.modelServices.ManufacturerService;

import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static ru.bikeservice.mainresources.config.SpringConfig.logger;

@Component
public class ApplicationEventsListener {
    private final ManufacturerService manufacturerService;
    private final PictureService pictureService;
    private final VerificationTokenService tokenService;
    private final UserService userService;
    private final ExecutorService mainExecutor;
    private final KafkaTemplate<String, byte[]> emailTemplate;

    @Autowired
    public ApplicationEventsListener(ManufacturerService manufacturerService,
                                     PictureService pictureService,
                                     VerificationTokenService tokenService,
                                     UserService userService,
                                     @Qualifier("MainExecutor")
                                     ExecutorService mainExecutor,
                                     KafkaTemplate<String, byte[]> emailTemplate) {
        this.manufacturerService = manufacturerService;
        this.pictureService = pictureService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.mainExecutor = mainExecutor;
        this.emailTemplate = emailTemplate;
    }

    //Checking for default manufacture in db.
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        checkStandardManufacturer();
        checkStandardPicture();
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent contextClosedEvent) {
//        userService.setNotActiveToAll();
        System.out.println("ContextClosedEvent occurred at millis: " + contextClosedEvent.getTimestamp());
    }

    @EventListener(OnRegistrationCompleteEvent.class)
    public void registration(OnRegistrationCompleteEvent event) {
        Runnable emailSend = () -> {
            UserModel user = event.getUserModel();
            String token = UUID.randomUUID().toString();

            tokenService.createVerificationToken(user, token);
            emailTemplate.send("emailRegistration", event.getBytes());
        };
        mainExecutor.submit(emailSend);
    }

    @EventListener(ResentTokenEvent.class)
    public void resentToken(ResentTokenEvent event) {
        Runnable emailSend = () -> emailTemplate.send("emailResend", event.getBytes());
        mainExecutor.submit(emailSend);
    }

    //LogIn tracking
    @EventListener(AuthenticationSuccessEvent.class)
    public void logInEvent(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        String userName = "null";
        if (principal instanceof UserModel) {
            userName = ((UserModel) principal).getName();
        } else if (principal instanceof OAuth2AuthenticationToken) {
            userName = ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("email");
        }
        logger.info("User " + userName + " is logged in.");
        userService.setActive(userName, true);
    }

    //LogOut tracking
    @EventListener(LogoutSuccessEvent.class)
    public void logOutEvent(LogoutSuccessEvent event) {
        String userName = event.getAuthentication().getName();
        logger.info("User " + userName + " is logged out.");
        userService.setActive(userName, false);
    }

    private void checkStandardManufacturer(){
        System.out.println("Checking default manufacture...");
        try {
            manufacturerService.getById(1L);
            System.out.println("Default manufacture is OK.");
        } catch (ResourceNotFoundException e) {
            Manufacturer defaultManufacture = new Manufacturer();
            defaultManufacture.setName("Default");
            defaultManufacture.setPicture(1L);
            defaultManufacture.setCreator(1L);
            defaultManufacture.setDescription("Default manufacture for everything");
            defaultManufacture.setLink("none");
            defaultManufacture.setValue("none");

            manufacturerService.save(defaultManufacture);
            System.out.println("Default manufacture was empty. New one is created in DB");
        }
    }

    private void checkStandardPicture(){
        System.out.println("Checking default picture...");
        try {
            pictureService.getById(1L);
            System.out.println("Default picture is OK.");
        } catch (ResourceNotFoundException e) {
            pictureService.save(new Picture("noImage.jpg"));
            System.out.println("Default picture was empty. New one is created in DB");
        }
    }
}

