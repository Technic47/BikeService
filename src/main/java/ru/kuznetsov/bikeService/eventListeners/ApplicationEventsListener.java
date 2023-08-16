package ru.kuznetsov.bikeService.eventListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.events.OnRegistrationCompleteEvent;
import ru.kuznetsov.bikeService.models.events.ResentTokenEvent;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.EmailService;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;

import java.util.concurrent.ExecutorService;

@Component
public class ApplicationEventsListener {
    private final PictureService pictureService;
    private final ManufacturerService manufacturerService;
    private final EmailService emailService;
    private final ExecutorService mainExecutor;

    @Autowired
    public ApplicationEventsListener(PictureService pictureService,
                                     ManufacturerService manufacturerService,
                                     EmailService emailService,
                                     @Qualifier("MainExecutor") ExecutorService mainExecutor) {
        this.pictureService = pictureService;
        this.manufacturerService = manufacturerService;
        this.emailService = emailService;
        this.mainExecutor = mainExecutor;
    }

    //Checking for default picture and manufacture in db.
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("Checking default picture...");
        if (pictureService.show(1L) == null) {
            pictureService.save(new Picture("noImage.jpg"));
            System.out.println("Default picture was empty. New one is created in DB");
        }
        System.out.println("Default picture is OK.");

        System.out.println("Checking default manufacture...");
        if (manufacturerService.show(1L) == null) {
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
        System.out.println("Default manufacture is OK.");
    }

    //Registration of new user.
    @EventListener(OnRegistrationCompleteEvent.class)
    public void registration(OnRegistrationCompleteEvent event) {
        Runnable emailSend = () -> {
            UserModel user = event.getUserModel();
            String appUrl = event.getAppUrl();
            emailService.constructSendVerificationEmail(user, appUrl);
        };
        mainExecutor.submit(emailSend);
    }

    //Resending verification email.
    @EventListener(ResentTokenEvent.class)
    public void resentToken(ResentTokenEvent event) {
        Runnable emailSend = () -> {
            String userEmail = event.getUserEmail();
            VerificationToken newToken = event.getToken();
            String appUrl = event.getAppUrl();
            emailService.constructResendVerificationTokenEmail(userEmail, newToken, appUrl);
        };
        mainExecutor.submit(emailSend);
    }
}

