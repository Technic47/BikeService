package ru.kuznetsov.bikeService.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.services.ManufacturerService;
import ru.kuznetsov.bikeService.services.PictureService;

@Component
public class EventListeners {
    private final PictureService pictureService;
    private final ManufacturerService manufacturerService;

    @Autowired
    public EventListeners(PictureService pictureService, ManufacturerService manufacturerService) {
        this.pictureService = pictureService;
        this.manufacturerService = manufacturerService;
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
}
