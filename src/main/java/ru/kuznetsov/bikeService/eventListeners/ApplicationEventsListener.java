package ru.kuznetsov.bikeService.eventListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import ru.bikeservice.mainresources.customExceptions.ResourceNotFoundException;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;
import ru.kuznetsov.bikeService.models.events.OnRegistrationCompleteEvent;
import ru.kuznetsov.bikeService.models.events.ResentTokenEvent;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;
import ru.kuznetsov.bikeService.services.VerificationTokenService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import static ru.kuznetsov.bikeService.controllers.abstracts.AbstractController.logger;

@Component
public class ApplicationEventsListener {
    private final PictureService pictureService;
    private final VerificationTokenService tokenService;
    private final UserService userService;
    private final ExecutorService mainExecutor;
    private final KafkaTemplate<String, byte[]> emailTemplate;
    private final ReplyingKafkaTemplate<String, ShowableGetter, List<AbstractShowableEntity>> mainResourcesKafkaTemplate;
    private final ReplyingKafkaTemplate<String, EntityKafkaTransfer, AbstractShowableEntity> creatorTemplate;

    @Autowired
    public ApplicationEventsListener(PictureService pictureService,
                                     VerificationTokenService tokenService, UserService userService, @Qualifier("MainExecutor") ExecutorService mainExecutor,
                                     KafkaTemplate<String, byte[]> emailTemplate,
                                     ReplyingKafkaTemplate<String, ShowableGetter, List<AbstractShowableEntity>> mainResourcesKafkaTemplate,
                                     ReplyingKafkaTemplate<String, EntityKafkaTransfer, AbstractShowableEntity> creatorTemlate) {
        this.pictureService = pictureService;
        this.tokenService = tokenService;
        this.userService = userService;
        this.mainExecutor = mainExecutor;
        this.emailTemplate = emailTemplate;
        this.mainResourcesKafkaTemplate = mainResourcesKafkaTemplate;
        this.creatorTemplate = creatorTemlate;
    }

    //Checking for default picture and manufacture in db.
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("Checking default picture...");
        try {
            pictureService.getById(1L);
            System.out.println("Default picture is OK.");
        } catch (ResourceNotFoundException e) {
            pictureService.save(new Picture("noImage.jpg"));
            System.out.println("Default picture was empty. New one is created in DB");
        }

//        System.out.println("Checking default manufacture...");
//        try {
//            ShowableGetter body = new ShowableGetter(Manufacturer.class.getSimpleName(), 1L);
//            ProducerRecord<String, ShowableGetter> record = new ProducerRecord<>("getItems", body);
//            RequestReplyFuture<String, ShowableGetter, List<AbstractShowableEntity>> reply = mainResourcesKafkaTemplate.sendAndReceive(record);
//
//            List<AbstractShowableEntity> objects = reply.get().value();
//            if (objects.isEmpty()) {
//                throw new ResourceNotFoundException(1L);
//            }
////            manufacturerService.getById(1L);
//            System.out.println("Default manufacture is OK.");
//        } catch (ResourceNotFoundException e) {
//            Manufacturer defaultManufacture = new Manufacturer();
//            defaultManufacture.setName("Default");
//            defaultManufacture.setPicture(1L);
//            defaultManufacture.setCreator(1L);
//            defaultManufacture.setDescription("Default manufacture for everything");
//            defaultManufacture.setLink("none");
//            defaultManufacture.setValue("none");
//
//            EntityKafkaTransfer body = new EntityKafkaTransfer(defaultManufacture, defaultManufacture.getClass().getSimpleName());
//            CompletableFuture<SendResult<String, EntityKafkaTransfer>> reply = creatorTemplate.send("createNewItem", body);
//
//            reply.thenRun(() -> System.out.println("Default manufacture was empty. New one is created in DB"));
//
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent contextClosedEvent) {
//        userService.setNotActiveToAll();
        System.out.println("ContextClosedEvent occurred at millis: " + contextClosedEvent.getTimestamp());
    }

    //Registration of new user.
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

    //Resending verification email.
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
}

