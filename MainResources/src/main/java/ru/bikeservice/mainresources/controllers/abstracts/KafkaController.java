package ru.bikeservice.mainresources.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.services.PictureService;
import ru.bikeservice.mainresources.services.modelServices.*;

public class KafkaController {
    public final static Logger logger = LoggerFactory.getLogger("MainServiceLogger");
    protected final PictureService pictureService;
    protected final DocumentService documentService;
    protected final FastenerService fastenerService;
    protected final ManufacturerService manufacturerService;
    protected final ConsumableService consumableService;
    protected final ToolService toolService;
    protected final PartService partService;
    protected final BikeService bikeService;

    public KafkaController(PictureService pictureService, DocumentService documentService, FastenerService fastenerService, ManufacturerService manufacturerService, ConsumableService consumableService, ToolService toolService, PartService partService, BikeService bikeService) {
        this.pictureService = pictureService;
        this.documentService = documentService;
        this.fastenerService = fastenerService;
        this.manufacturerService = manufacturerService;
        this.consumableService = consumableService;
        this.toolService = toolService;
        this.partService = partService;
        this.bikeService = bikeService;
    }

    @KafkaListener(topics = "getItem", id = "mainResources")
    @SendTo
    public Showable getShowable(ShowableGetter requestDTO) {
        Showable item = null;
        String category = requestDTO.getCategory();
        switch (category) {
            case "documents" -> item = documentService.getById(requestDTO.getId());
            case "fasteners" -> item = fastenerService.getById(requestDTO.getId());
            case "manufacturers" -> item = manufacturerService.getById(requestDTO.getId());
            case "consumables" -> item = consumableService.getById(requestDTO.getId());
            case "tools" -> item = toolService.getById(requestDTO.getId());
            case "parts" -> item = partService.getById(requestDTO.getId());
            case "bikes" -> item = bikeService.getById(requestDTO.getId());
        }
        return item;
    }
}
