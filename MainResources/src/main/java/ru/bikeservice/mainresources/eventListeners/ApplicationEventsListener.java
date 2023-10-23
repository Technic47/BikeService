package ru.bikeservice.mainresources.eventListeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.bikeservice.mainresources.customExceptions.ResourceNotFoundException;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.services.modelServices.ManufacturerService;

@Component
public class ApplicationEventsListener {
    private final ManufacturerService manufacturerService;

    @Autowired
    public ApplicationEventsListener(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    //Checking for default manufacture in db.
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
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
}

