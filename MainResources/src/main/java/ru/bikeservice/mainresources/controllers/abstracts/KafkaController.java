package ru.bikeservice.mainresources.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.services.PictureService;
import ru.bikeservice.mainresources.services.modelServices.*;

import java.util.ArrayList;
import java.util.List;

public class KafkaController {
    public final static Logger logger = LoggerFactory.getLogger("MainResourcesLogger");
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

    @KafkaListener(topics = "getItems", id = "mainResources")
    @SendTo
    public List<Showable> getShowable(ShowableGetter requestDTO) {
        List<Showable> list = new ArrayList<>(1);
        String category = requestDTO.getCategory();
        switch (category) {
            case "documents" -> list.add(documentService.getById(requestDTO.getId()));
            case "fasteners" -> list.add(fastenerService.getById(requestDTO.getId()));
            case "manufacturers" -> list.add(manufacturerService.getById(requestDTO.getId()));
            case "consumables" -> list.add(consumableService.getById(requestDTO.getId()));
            case "tools" -> list.add(toolService.getById(requestDTO.getId()));
            case "parts" -> list.add(partService.getById(requestDTO.getId()));
            case "bikes" -> list.add(bikeService.getById(requestDTO.getId()));
        }
        return list;
    }
}
