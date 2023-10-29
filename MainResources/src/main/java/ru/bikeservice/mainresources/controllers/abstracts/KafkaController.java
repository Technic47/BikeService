package ru.bikeservice.mainresources.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.KafkaUserDto;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.dto.kafka.IndexKafkaDTO;
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
import ru.bikeservice.mainresources.services.SearchService;
import ru.bikeservice.mainresources.services.abstracts.AbstractServiceableService;
import ru.bikeservice.mainresources.services.modelServices.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Component
public class KafkaController {
    public final static Logger logger = LoggerFactory.getLogger("MainResourcesLogger");
    protected final DocumentService documentService;
    protected final FastenerService fastenerService;
    protected final ManufacturerService manufacturerService;
    protected final ConsumableService consumableService;
    protected final ToolService toolService;
    protected final PartService partService;
    protected final BikeService bikeService;
    protected final ExecutorService mainExecutor;
    protected final SearchService searchService;

    public KafkaController(DocumentService documentService,
                           FastenerService fastenerService,
                           ManufacturerService manufacturerService,
                           ConsumableService consumableService,
                           ToolService toolService,
                           PartService partService,
                           BikeService bikeService,
                           @Qualifier("MainExecutor") ExecutorService mainExecutor,
                           SearchService searchService) {
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

    @KafkaListener(topics = "showEntity",
            groupId = "showEntity",
            containerFactory = "showListenerContainerFactory")
    @SendTo
    public EntityKafkaTransfer getShowable(ShowableGetter requestDTO) {
        Showable item = null;
        String category = requestDTO.getType();
        switch (category) {
            case "Document" -> item = documentService.getById(requestDTO.getItemId());
            case "Fastener" -> item = fastenerService.getById(requestDTO.getItemId());
            case "Manufacturer" -> item = manufacturerService.getById(requestDTO.getItemId());
            case "Consumable" -> item = consumableService.getById(requestDTO.getItemId());
            case "Tool" -> item = toolService.getById(requestDTO.getItemId());
            case "Part" -> item = partService.getById(requestDTO.getItemId());
            case "Bike" -> item = bikeService.getById(requestDTO.getItemId());
        }

        if (item != null) {
            logger.info(category + " with id: " + requestDTO.getItemId() + " was found.");
            return new EntityKafkaTransfer(item, category);
        } else {
            logger.info(category + " with id: " + requestDTO.getItemId() + " not found.");
            return new EntityKafkaTransfer();
        }
    }

    @KafkaListener(topics = "showIndex",
            groupId = "showIndex",
            containerFactory = "indexListenerContainerFactory")
    @SendTo
    public EntityKafkaTransfer[] index(IndexKafkaDTO requestDTO) {
        List<Showable> list = new ArrayList<>();
        String category = requestDTO.getType();
        if (!requestDTO.isAdmin()) {
            if (requestDTO.isShared()) {
                switch (category) {
                    case "Document" -> list.addAll(documentService.findByCreatorOrShared(requestDTO.getUserId()));
                    case "Fastener" -> list.addAll(fastenerService.findByCreatorOrShared(requestDTO.getUserId()));
                    case "Manufacturer" ->
                            list.addAll(manufacturerService.findByCreatorOrShared(requestDTO.getUserId()));
                    case "Consumable" -> list.addAll(consumableService.findByCreatorOrShared(requestDTO.getUserId()));
                    case "Tool" -> list.addAll(toolService.findByCreatorOrShared(requestDTO.getUserId()));
                    case "Part" -> list.addAll(partService.findByCreatorOrShared(requestDTO.getUserId()));
                    case "Bike" -> list.addAll(bikeService.findByCreatorOrShared(requestDTO.getUserId()));
                }
            } else {
                switch (category) {
                    case "Document" -> list.addAll(documentService.findByCreator(requestDTO.getUserId()));
                    case "Fastener" -> list.addAll(fastenerService.findByCreator(requestDTO.getUserId()));
                    case "Manufacturer" -> list.addAll(manufacturerService.findByCreator(requestDTO.getUserId()));
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
                case "Manufacturer" -> list.addAll(manufacturerService.index());
                case "Consumable" -> list.addAll(consumableService.index());
                case "Tool" -> list.addAll(toolService.index());
                case "Part" -> list.addAll(partService.index());
                case "Bike" -> list.addAll(bikeService.index());
            }
        }
        System.out.println("Index is done.");
        System.out.println(list.size() + " items in list.");

        EntityKafkaTransfer[] results = new EntityKafkaTransfer[list.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = new EntityKafkaTransfer(list.get(i), category);
        }

        return results;
    }

    @KafkaListener(topics = "saveEntity",
            id = "saveEntity",
            containerFactory = "entityListenerContainerFactory")
    @SendTo
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    public EntityKafkaTransfer createEntity(EntityKafkaTransfer dtoNew) {
        Showable item = null;
        String type = dtoNew.getType();
        switch (type) {
            case "Document" -> item = documentService.save(new Document(dtoNew));
            case "Fastener" -> item = fastenerService.save(new Fastener(dtoNew));
            case "Manufacturer" -> item = manufacturerService.save(new Manufacturer(dtoNew));
            case "Consumable" -> item = consumableService.save(new Consumable(dtoNew));
            case "Tool" -> item = toolService.save(new Tool(dtoNew));
            case "Part" -> item = partService.save(new Part(dtoNew));
            case "Bike" -> item = bikeService.save(new Bike(dtoNew));
        }

        if (item != null) {
            logger.info(type + " with id: " + item.getId() + " was created.");
            return new EntityKafkaTransfer(item, type);
        } else {
            logger.info("Creation of " + type + " is not finished.");
            return new EntityKafkaTransfer();
        }
    }

    @KafkaListener(topics = "updateEntity",
            id = "updateEntity",
            containerFactory = "entityListenerContainerFactory")
    @SendTo
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    public EntityKafkaTransfer updateEntity(EntityKafkaTransfer dtoToUpdate) {
        AbstractShowableEntity updated = null;
        String type = dtoToUpdate.getType();
        switch (type) {
            case "Document" -> updated = documentService.update(dtoToUpdate.getId(), new Document(dtoToUpdate));
            case "Fastener" -> updated = fastenerService.update(dtoToUpdate.getId(), new Fastener(dtoToUpdate));
            case "Manufacturer" ->
                    updated = manufacturerService.update(dtoToUpdate.getId(), new Manufacturer(dtoToUpdate));
            case "Consumable" -> updated = consumableService.update(dtoToUpdate.getId(), new Consumable(dtoToUpdate));
            case "Tool" -> updated = toolService.update(dtoToUpdate.getId(), new Tool(dtoToUpdate));
            case "Part" -> updated = partService.update(dtoToUpdate.getId(), new Part(dtoToUpdate));
            case "Bike" -> updated = bikeService.update(dtoToUpdate.getId(), new Bike(dtoToUpdate));
        }

        if (updated != null) {
            logger.info(type + " with id: " + updated.getId() + " was updated.");
            return new EntityKafkaTransfer(updated, type);
        } else {
            logger.info("Update of " + type + " is not finished.");
            return new EntityKafkaTransfer();
        }
    }

    @KafkaListener(topics = "deleteItem",
            id = "deleteEntity",
            containerFactory = "entityListenerContainerFactory")
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    public void deleteEntity(EntityKafkaTransfer dtoToDelete) {
        switch (dtoToDelete.getType()) {
            case "Document" -> documentService.delete(dtoToDelete.getId());
            case "Fastener" -> fastenerService.delete(dtoToDelete.getId());
            case "Manufacturer" -> manufacturerService.delete(dtoToDelete.getId());
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

    @KafkaListener(topics = "search",
            id = "searchService",
    containerFactory = "searchListenerContainerFactory")
    public EntityKafkaTransfer[] search(SearchKafkaDTO searchDto) {
        List<AbstractShowableEntity> list;

        String findBy = searchDto.getFindBy();
        String searchValue = searchDto.getSearchValue();
        KafkaUserDto kafkaUserDto = searchDto.getUserDTO();
        boolean shared = searchDto.isShared();
        String category = searchDto.getType();

        try {
            if (category != null) {
                list = searchService.doSearchProcedure(findBy, searchValue, kafkaUserDto, shared, category);
            } else list = searchService.doGlobalSearchProcedure(findBy, searchValue, kafkaUserDto, shared);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        EntityKafkaTransfer[] results = new EntityKafkaTransfer[list.size()];
        for (int i = 0; i < results.length; i++) {
            results[i] = new EntityKafkaTransfer(list.get(i), category);
        }

        return results;
    }

    @KafkaListener(topics = "addLinkedItem",
            id = "addLinkedItem")
    public AbstractShowableEntity addLinkedItem(EntityKafkaTransfer toAdd) {
        AbstractServiceableEntity item;
        switch (toAdd.getType()) {
            case "Part" -> {
                item = partService.getById(toAdd.getId());
                setLinkedItems(item, partService, toAdd.getLinkedItems(), 1);
            }
            case "Bike" -> {
                item = bikeService.getById(toAdd.getId());
                setLinkedItems(item, bikeService, toAdd.getLinkedItems(), 1);
            }
            default -> throw new IllegalArgumentException("Wrong type of entity");
        }
        return item;
    }

    @KafkaListener(topics = "delLinkedItem", id = "delLinkedItem")
    public AbstractServiceableEntity delLinkedItem(EntityKafkaTransfer toAdd) {
        AbstractServiceableEntity item;
        switch (toAdd.getType()) {
            case "Part" -> {
                item = partService.getById(toAdd.getId());
                return setLinkedItems(item, partService, toAdd.getLinkedItems(), 0);
            }
            case "Bike" -> {
                item = bikeService.getById(toAdd.getId());
                return setLinkedItems(item, bikeService, toAdd.getLinkedItems(), 0);
            }
            default -> throw new IllegalArgumentException("Wrong type of entity");
        }
    }

    private AbstractServiceableEntity setLinkedItems(AbstractServiceableEntity item,
                                                     AbstractServiceableService service,
                                                     Collection<PartEntity> addList,
                                                     int action) {
        switch (action) {
            case 1 -> {
                for (PartEntity partEntity : addList) {
                    service.addToLinkedItems(item, partEntity);
                }
            }
            case 0 -> {
                for (PartEntity partEntity : addList) {
                    service.delFromLinkedItems(item, partEntity);
                }
            }
        }
        return item;
    }
}
