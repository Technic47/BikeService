package ru.bikeservice.mainresources.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.KafkaUserDto;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.dto.kafka.SearchKafkaDTO;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.services.PictureService;
import ru.bikeservice.mainresources.services.SearchService;
import ru.bikeservice.mainresources.services.modelServices.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

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
    protected final ExecutorService mainExecutor;
    protected final SearchService searchService;

    public KafkaController(PictureService pictureService, DocumentService documentService, FastenerService fastenerService, ManufacturerService manufacturerService, ConsumableService consumableService, ToolService toolService, PartService partService, BikeService bikeService, ExecutorService mainExecutor, SearchService searchService) {
        this.pictureService = pictureService;
        this.documentService = documentService;
        this.fastenerService = fastenerService;
        this.manufacturerService = manufacturerService;
        this.consumableService = consumableService;
        this.toolService = toolService;
        this.partService = partService;
        this.bikeService = bikeService;
        this.mainExecutor = mainExecutor;
        this.searchService = searchService;
    }

    @KafkaListener(topics = "getItems", id = "mainResources")
    @SendTo
    public List<Showable> getShowable(ShowableGetter requestDTO) {
        if (requestDTO.getItemId() != null) {
            List<Showable> list = new ArrayList<>(1);
            String category = requestDTO.getType();
            switch (category) {
                case "Document" -> list.add(documentService.getById(requestDTO.getItemId()));
                case "Fastener" -> list.add(fastenerService.getById(requestDTO.getItemId()));
                case "Manufacture" -> list.add(manufacturerService.getById(requestDTO.getItemId()));
                case "Consumable" -> list.add(consumableService.getById(requestDTO.getItemId()));
                case "Tool" -> list.add(toolService.getById(requestDTO.getItemId()));
                case "Part" -> list.add(partService.getById(requestDTO.getItemId()));
                case "Bike" -> list.add(bikeService.getById(requestDTO.getItemId()));
            }
            return list;
        }

        if (requestDTO.getUserId() != null) {
            List<Showable> list = new ArrayList<>();
            String category = requestDTO.getType();
            if (!requestDTO.isAdmin()) {
                if (requestDTO.isShared()) {
                    switch (category) {
                        case "Document" -> list.addAll(documentService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "Fastener" -> list.addAll(fastenerService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "Manufacture" ->
                                list.addAll(manufacturerService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "Consumable" ->
                                list.addAll(consumableService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "Tool" -> list.addAll(toolService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "Part" -> list.addAll(partService.findByCreatorOrShared(requestDTO.getUserId()));
                        case "Bike" -> list.addAll(bikeService.findByCreatorOrShared(requestDTO.getUserId()));
                    }
                } else {
                    switch (category) {
                        case "Document" -> list.addAll(documentService.findByCreator(requestDTO.getUserId()));
                        case "Fastener" -> list.addAll(fastenerService.findByCreator(requestDTO.getUserId()));
                        case "Manufacture" -> list.addAll(manufacturerService.findByCreator(requestDTO.getUserId()));
                        case "Consumable" -> list.addAll(consumableService.findByCreator(requestDTO.getUserId()));
                        case "Tool" -> list.addAll(toolService.findByCreator(requestDTO.getUserId()));
                        case "Part" -> list.addAll(partService.findByCreator(requestDTO.getUserId()));
                        case "Bike" -> list.addAll(bikeService.findByCreator(requestDTO.getUserId()));
                    }
                }
            } else {
                switch (category) {
                    case "Document" -> list.addAll(documentService.index());
                    case "Fastener" -> list.addAll(fastenerService.index());
                    case "Manufacture" -> list.addAll(manufacturerService.index());
                    case "Consumable" -> list.addAll(consumableService.index());
                    case "Tool" -> list.addAll(toolService.index());
                    case "Part" -> list.addAll(partService.index());
                    case "Bike" -> list.addAll(bikeService.index());
                }
            }
            return list;
        }
        return null;
    }

    @KafkaListener(topics = "createNewItem", id = "mainResources")
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    public void createEntity(EntityKafkaTransfer dtoNew) {
        switch (dtoNew.getType()) {
            case "Document" -> documentService.save(new Document(dtoNew));
            case "Fastener" -> fastenerService.save(new Fastener(dtoNew));
            case "Manufacture" -> manufacturerService.save(new Manufacturer(dtoNew));
            case "Consumable" -> consumableService.save(new Consumable(dtoNew));
            case "Tool" -> toolService.save(new Tool(dtoNew));
            case "Part" -> partService.save(new Part(dtoNew));
            case "Bike" -> bikeService.save(new Bike(dtoNew));
        }
    }

    @KafkaListener(topics = "updateItem", id = "mainResources")
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    public AbstractShowableEntity updateEntity(EntityKafkaTransfer dtoToUpdate) {
        AbstractShowableEntity updated = null;
        switch (dtoToUpdate.getType()) {
            case "Document" -> updated = documentService.update(dtoToUpdate.getId(), new Document(dtoToUpdate));
            case "Fastener" -> updated = fastenerService.update(dtoToUpdate.getId(), new Fastener(dtoToUpdate));
            case "Manufacture" ->
                    updated = manufacturerService.update(dtoToUpdate.getId(), new Manufacturer(dtoToUpdate));
            case "Consumable" -> updated = consumableService.update(dtoToUpdate.getId(), new Consumable(dtoToUpdate));
            case "Tool" -> updated = toolService.update(dtoToUpdate.getId(), new Tool(dtoToUpdate));
            case "Part" -> updated = partService.update(dtoToUpdate.getId(), new Part(dtoToUpdate));
            case "Bike" -> updated = bikeService.update(dtoToUpdate.getId(), new Bike(dtoToUpdate));
        }
        return updated;
    }

    @KafkaListener(topics = "deleteItem", id = "mainResources")
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    public void deleteEntity(EntityKafkaTransfer dtoToDelete) {
        switch (dtoToDelete.getType()) {
            case "Document" -> documentService.delete(dtoToDelete.getId());
            case "Fastener" -> fastenerService.delete(dtoToDelete.getId());
            case "Manufacture" -> manufacturerService.delete(dtoToDelete.getId());
            case "Consumable" -> consumableService.delete(dtoToDelete.getId());
            case "Tool" -> toolService.delete(dtoToDelete.getId());
            case "Part" -> partService.delete(dtoToDelete.getId());
            case "Bike" -> bikeService.delete(dtoToDelete.getId());
        }
        cleanUpAfterDelete(dtoToDelete);
    }

    /**
     * Checks if entity is linked to any Part or Bike and deletes it from linkedItems.
     *
     * @param item entity to delete.
     */
    private void cleanUpAfterDelete(
            EntityKafkaTransfer item) {
        String partType = item.getType();
        Long id = item.getId();
        Runnable clearParts = () -> {
            PartEntity entityPart = new PartEntity("Part", partType, id, 1);
            List<Part> listOfParts = this.partService.findByLinkedItemsItemIdAndLinkedItemsType(id, partType);
            listOfParts.parallelStream().forEach(part -> partService.delFromLinkedItems(part, entityPart));
        };
        Runnable clearBikes = () -> {
            PartEntity entityBike = new PartEntity("Bike", partType, id, 1);
            List<Bike> listOfBikes = this.bikeService.findByLinkedPartsItemIdAndLinkedPartsType(id, partType);
            listOfBikes.parallelStream().forEach(part -> bikeService.delFromLinkedItems(part, entityBike));
        };
        mainExecutor.submit(clearParts);
        mainExecutor.submit(clearBikes);
    }

    @KafkaListener(topics = "search", id = "searchService")
    public List<AbstractShowableEntity> search(SearchKafkaDTO searchDto) {
        List<AbstractShowableEntity> results = null;

        String findBy = searchDto.getFindBy();
        String searchValue = searchDto.getSearchValue();
        KafkaUserDto kafkaUserDto = searchDto.getUserDTO();
        boolean shared = searchDto.isShared();
        String category = searchDto.getCategory();

        try {
            if (category != null) {
                results = searchService.doSearchProcedure(findBy, searchValue, kafkaUserDto, shared, category);
            } else results = searchService.doGlobalSearchProcedure(findBy, searchValue, kafkaUserDto, shared);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
}
