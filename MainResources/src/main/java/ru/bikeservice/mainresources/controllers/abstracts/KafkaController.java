package ru.bikeservice.mainresources.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.services.PictureService;
import ru.bikeservice.mainresources.services.modelServices.*;

import java.util.ArrayList;
import java.util.List;

import static ru.bikeservice.mainresources.models.dto.UserRole.ROLE_ADMIN;
import static ru.bikeservice.mainresources.models.dto.UserRole.ROLE_USER;

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
        if (requestDTO.getId() != null) {
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

        if (requestDTO.getUserId() != null) {
            List<Showable> list = new ArrayList<>();
            String category = requestDTO.getCategory();
            if (requestDTO.getRole().contains(ROLE_USER)) {
                if (requestDTO.isShared()) {
                    switch (category) {
                        case "documents" -> list.addAll(documentService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "fasteners" -> list.addAll(fastenerService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "manufacturers" ->
                                list.addAll(manufacturerService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "consumables" ->
                                list.addAll(consumableService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "tools" -> list.addAll(toolService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "parts" -> list.addAll(partService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "bikes" -> list.addAll(bikeService.findByCreatorOrShared(requestDTO.getUserId()));
                    }
                } else {
                    switch (category) {
                        case "documents" -> list.addAll(documentService.findByCreator(requestDTO.getUserId()));
                        case "fasteners" -> list.addAll(fastenerService.findByCreator(requestDTO.getUserId()));
                        case "manufacturers" -> list.addAll(manufacturerService.findByCreator(requestDTO.getUserId()));
                        case "consumables" -> list.addAll(consumableService.findByCreator(requestDTO.getUserId()));
                        case "tools" -> list.addAll(toolService.findByCreator(requestDTO.getUserId()));
                        case "parts" -> list.addAll(partService.findByCreator(requestDTO.getUserId()));
                        case "bikes" -> list.addAll(bikeService.findByCreator(requestDTO.getUserId()));
                    }
                }
            }
            if (requestDTO.getRole().contains(ROLE_ADMIN)) {
                switch (category) {
                    case "documents" -> list.addAll(documentService.index());
                    case "fasteners" -> list.addAll(fastenerService.index());
                    case "manufacturers" -> list.addAll(manufacturerService.index());
                    case "consumables" -> list.addAll(consumableService.index());
                    case "tools" -> list.addAll(toolService.index());
                    case "parts" -> list.addAll(partService.index());
                    case "bikes" -> list.addAll(bikeService.index());
                }
            }
            return list;
        }
        return null;
    }

    @KafkaListener(topics = "{createShowable, createUsable, createServiceable}", id = "mainResources")
    public void createEntity(AbstractEntityDtoNew dtoNew){
        switch (dtoNew.getCategory()) {
            case "documents" -> documentService.save(new Document(dtoNew));
            case "fasteners" -> fastenerService.save(new Fastener(dtoNew));
            case "manufacturers" -> manufacturerService.save(new Manufacturer(dtoNew));
            case "consumables" -> consumableService.save(new Consumable(dtoNew));
            case "tools" -> toolService.save(new Tool(dtoNew));
            case "parts" -> partService.save(new Part(dtoNew));
            case "bikes" -> bikeService.save(new Bike(dtoNew));
        }
    }
}
