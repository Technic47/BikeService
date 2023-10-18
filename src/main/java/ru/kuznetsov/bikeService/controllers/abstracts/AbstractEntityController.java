package ru.kuznetsov.bikeService.controllers.abstracts;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.abstracts.AbstractUsableEntity;
import ru.bikeservice.mainresources.models.dto.KafkaUserDto;
import ru.bikeservice.mainresources.models.dto.PdfEntityDto;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.dto.kafka.SearchKafkaDTO;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;
import ru.bikeservice.mainresources.models.lists.ServiceList;
import ru.bikeservice.mainresources.models.lists.UserEntity;
import ru.bikeservice.mainresources.models.pictures.Picture;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.services.SearchService;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;

/**
 * Intermediate layer that includes common methods for REST and non-REST controllers.
 */
public abstract class AbstractEntityController extends AbstractController {
    protected SearchService searchService;
    @Autowired
    private ReplyingKafkaTemplate<String, PdfEntityDto, byte[]> pdfKafkaTemplate;
    @Autowired
    protected ReplyingKafkaTemplate<String, ShowableGetter, List<AbstractShowableEntity>> mainResourcesKafkaTemplate;
    @Autowired
    protected ReplyingKafkaTemplate<String, EntityKafkaTransfer, AbstractShowableEntity> creatorTemlate;
    @Autowired
    protected ReplyingKafkaTemplate<String, SearchKafkaDTO, List<AbstractShowableEntity>> searchTemplate;

    /**
     * Creates List of entities depending on userModel role and shared flag.
     *
     * @param userModel user for whom List is being created.
     * @param category  category of entities for logging.
     * @param shared    flag for including shared entities.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     * @return formed List.
     */
    protected <T extends AbstractShowableEntity> List<T> doIndexProcedure(final UserModel userModel, final String category, boolean shared) {
        List<T> objects = null;
        try {
            if (userModel.getAuthorities().contains(ROLE_USER)) {
                ShowableGetter body = new ShowableGetter(category, null, userModel.getId(), userModel.getAuthorities().contains(ROLE_ADMIN), shared);
                ProducerRecord<String, ShowableGetter> record = new ProducerRecord<>("getItems", body);
                RequestReplyFuture<String, ShowableGetter, List<AbstractShowableEntity>> reply = mainResourcesKafkaTemplate.sendAndReceive(record);

                objects = (List<T>) reply.get().value();
                if (shared) {
//                    objects = service.findByCreatorOrShared(userModel.getId());
                    logger.info("Personal and shared " + category + " are shown to '" + userModel.getUsername() + "'");
                } else {
//                    objects = service.findByCreator(userModel.getId());
                    logger.info("Personal " + category + " are shown to '" + userModel.getUsername() + "'");
                }
            }
            if (userModel.getAuthorities().contains(ROLE_ADMIN)) {

//                objects = service.index();
                logger.info("All " + category + " are shown to '" + userModel.getUsername() + "'");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return objects;
    }

    /**
     * Form and add Maps with owned and created entities. Add Maps to provided Model object.
     *
     * @param model   Model object where to put formed Maps.
     * @param userId  Id of user. User to for creation check in filtering results.
     * @param objects List of objects.
     * @param <T>     AbstractShowableEntity from main mainResources.models.
     */
    protected <T extends AbstractShowableEntity> void addIndexMapsToModel(
            Model model, final Long userId, final List<T> objects) {
        Map<T, String> indexMap = new HashMap<>();
        Map<T, String> sharedIndexMap = new HashMap<>();
        if (objects != null) {
            objects.forEach(object -> {
                if (object.getCreator().equals(userId)) {
                    indexMap.put(object, pictureService.getById(object.getPicture()).getName());
                } else sharedIndexMap.put(object, pictureService.getById(object.getPicture()).getName());
            });
        }
        model.addAttribute("objects", indexMap);
        model.addAttribute("sharedObjects", sharedIndexMap);
    }

    /**
     * Main procedure for showing entity.
     *
     * @param type      type of entities.
     * @param id        id of entity.
     * @param principal principal to whom entity is shown.
     * @param <T>       AbstractShowableEntity from main models.
     * @return entity.
     */
    protected <T extends AbstractShowableEntity> T doShowProcedure(
            final String type, final Long id, Principal principal
    ) {
        ShowableGetter body = new ShowableGetter(type, id);
        ProducerRecord<String, ShowableGetter> record = new ProducerRecord<>("getItems", body);
        RequestReplyFuture<String, ShowableGetter, List<AbstractShowableEntity>> reply = mainResourcesKafkaTemplate.sendAndReceive(record);

        T item;
        try {
            item = (T) reply.get().value().get(0);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        logger.info(item.getClass()
                .getSimpleName() + " " + item.getId() + " was shown to '" + this.getUserModelFromPrincipal(principal).getUsername() + "'");
        return item;
    }

    /**
     * Main procedure for creation new entity.
     *
     * @param item      entity with new information.
     * @param file      Multipart file with picture.
     * @param principal principal who is updating.
     * @param type      type of entities.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     * @return updated entity.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    protected <T extends AbstractShowableEntity>
    T doCreateProcedure(final T item, MultipartFile file, Principal principal, final String type) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        EntityKafkaTransfer body = new EntityKafkaTransfer(item, type);
        this.checkImageFile(file, body);
        body.setCreator(userModel.getId());

        ProducerRecord<String, EntityKafkaTransfer> record = new ProducerRecord<>("createNewItem", body);
        RequestReplyFuture<String, EntityKafkaTransfer, AbstractShowableEntity> reply = creatorTemlate.sendAndReceive(record);

        T createdItem;
        try {
            createdItem = (T) reply.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
//        item.setCreated(new Date());
//        T createdItem = service.save(item);
        userService.addCreatedItem(userModel,
                new UserEntity(item.getClass().getSimpleName(), createdItem.getId()));
        logger.info(type + " " + item.getId() + ":" + item.getName() + " was created by '" + userModel.getUsername());
        return createdItem;
    }

    /**
     * Main procedure for updating entity.
     *
     * @param newItem   entity with new information.
     * @param type      type of entities.
     * @param oldItem   entity to apply new information.
     * @param file      Multipart file with picture.
     * @param principal principal who is updating.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     * @return updated entity.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    protected <T extends AbstractShowableEntity>
    T doUpdateProcedure(final T newItem, String type, final T oldItem, MultipartFile file, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);

        EntityKafkaTransfer body = new EntityKafkaTransfer(newItem, type);
        body.setId(oldItem.getId());
        this.checkImageFile(file, body);
        ProducerRecord<String, EntityKafkaTransfer> record = new ProducerRecord<>("updateItem", body);
        RequestReplyFuture<String, EntityKafkaTransfer, AbstractShowableEntity> reply = creatorTemlate.sendAndReceive(record);

        T updated;
        try {
            updated = (T) reply.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

//        T updated = service.update(oldItem, newItem);

        logger.info(newItem.getClass()
                .getSimpleName() + " id:" + oldItem.getId() + " was edited by '" + userModel.getUsername() + "'");
        return updated;
    }

    /**
     * Main procedure for deleting entity. Cleans up connected tables.
     *
     * @param item      entity for deleting.
     * @param type      type of entities.
     * @param principal principal who is trying to delete the item.
     * @param <T>       AbstractShowableEntity from main models.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    protected <T extends AbstractShowableEntity>
    void doDeleteProcedure(final T item, String type, final Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Long itemId = item.getId();
        EntityKafkaTransfer body = new EntityKafkaTransfer(item, type);

        CompletableFuture<SendResult<String, EntityKafkaTransfer>> reply = creatorTemlate.send("updateItem", body);

        try {
            reply.thenRun(() -> this.cleanUpUserAfterDelete(item, userModel));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        service.delete(itemId);
//        this.cleanUpAfterDelete(item, userModel);
        logger.info(type +
                " id:" + itemId + " was deleted by '" + userModel.getUsername() + "'");
    }

    /**
     * Deletes entity record from user createdList.
     *
     * @param item      entity to delete.
     * @param userModel creator of entity.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     */
    private <T extends AbstractShowableEntity> void cleanUpUserAfterDelete(
            T item, UserModel userModel) {
//        Runnable clearParts = () -> {
//            PartEntity entityPart = new PartEntity("Part", partType, id, 1);
//            List<Part> listOfParts = this.partService.findByLinkedItemsItemIdAndLinkedItemsType(id, partType);
//            listOfParts.parallelStream().forEach(part -> partService.delFromLinkedItems(part, entityPart));
//        };
//        Runnable clearBikes = () -> {
//            PartEntity entityBike = new PartEntity("Bike", partType, id, 1);
//            List<Bike> listOfBikes = this.bikeService.findByLinkedPartsItemIdAndLinkedPartsType(id, partType);
//            listOfBikes.parallelStream().forEach(part -> bikeService.delFromLinkedItems(part, entityBike));
//        };
        mainExecutor.submit(() -> userService
                .delCreatedItem(userModel, new UserEntity(item)));
//        mainExecutor.submit(clearParts);
//        mainExecutor.submit(clearBikes);
    }

    protected List<AbstractShowableEntity> doSearchProcedure(String findBy, String searchValue, KafkaUserDto kafkaUserDto, boolean shared, String category) {
        SearchKafkaDTO body = new SearchKafkaDTO(findBy, searchValue, kafkaUserDto, shared, category);

        ProducerRecord<String, SearchKafkaDTO> record = new ProducerRecord<>("search", body);
        RequestReplyFuture<String, SearchKafkaDTO, List<AbstractShowableEntity>> reply = searchTemplate.sendAndReceive(record);

        List<AbstractShowableEntity> results;
        try {
            results = reply.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return results;
    }

    /**
     * Checking access of Principal to specified entity.
     *
     * @param item      secured entity.
     * @param principal Principal to check.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     * @return access boolean.
     */
    protected <T extends AbstractShowableEntity> boolean checkAccessToItem(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            this.logAccess(item, userModel.getUsername(), true);
            return true;
        } else {
            boolean access = item.getCreator().equals(userModel.getId());
            this.logAccess(item, userModel.getUsername(), access);
            return access;
        }
    }

    protected <T extends AbstractShowableEntity> ResponseEntity<Resource> prepareShowablePDF(
            T item, Principal principal
    ) {
        try {
            PdfEntityDto body = this.buildShowableDTO(item, principal);
            return preparePDF(body);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected <T extends AbstractUsableEntity> ResponseEntity<Resource> prepareUsablePDF(
            T item, Principal principal
    ) {
        Manufacturer manufacturer = manufacturerService.getById(item.getManufacturer());

        try {
            PdfEntityDto body = this.buildShowableDTO(item, principal);
            body.setManufacturer(manufacturer.getName());
            return preparePDF(body);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    protected <T extends AbstractServiceableEntity> ResponseEntity<Resource> prepareServiceablePDF(
            T item, Principal principal, ServiceList serviceList
    ) {
        Manufacturer manufacturer = manufacturerService.getById(item.getManufacturer());

        try {
            PdfEntityDto body = this.buildShowableDTO(item, principal);
            body.setManufacturer(manufacturer.getName());
            body.setLinkedItems(serviceList);
            return preparePDF(body);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T extends AbstractShowableEntity> PdfEntityDto buildShowableDTO(
            T item, Principal principal
    ) throws IOException {
        UserModel userModel = getUserModelFromPrincipal(principal);
        Path path = pictureService.getPicturePath(item.getPicture());
        return new PdfEntityDto(item, userModel.getUsername(), Files.readAllBytes(path));
    }

    private void logAccess(Showable item, String userName, boolean access) {
        logger.info("User " + userName + " tries to get access to item: "
                + item.getClass().getSimpleName() + " id: " + item.getId() + " access - " + access);
    }

    /**
     * Checks if Multipart file contains picture. Set picture to entity if contains.
     *
     * @param file file to check.
     * @param item entity to set picture.
     * @param <T>  AbstractShowableEntity from main models.
     */
    protected <T extends AbstractShowableEntity> void checkImageFile(MultipartFile file, T item) {
        if (file != null) {
            if (!file.isEmpty()) {
                Picture picture = pictureService.save(file);
                item.setPicture(picture.getId());
            }
        }
        if (item.getPicture() == null) {
            item.setPicture(1L);
        }
    }

    /**
     * Checks if Multipart file contains picture. Set picture to entity if contains.
     *
     * @param file file to check.
     * @param item entity to set picture.
     * @param <T>  AbstractShowableEntity from main models.
     */
    protected <T extends AbstractShowableEntity> void checkImageFile(MultipartFile file, EntityKafkaTransfer item) {
        if (file != null) {
            if (!file.isEmpty()) {
                Picture picture = pictureService.save(file);
                item.setPicture(picture.getId());
            }
        }
        if (item.getPicture() == null) {
            item.setPicture(1L);
        }
    }

    protected ResponseEntity<Resource> preparePDF(PdfEntityDto body) {
        ProducerRecord<String, PdfEntityDto> record = new ProducerRecord<>("pdf", body);
        RequestReplyFuture<String, PdfEntityDto, byte[]> reply = pdfKafkaTemplate
                .sendAndReceive(record);

        try {
            ByteArrayResource resource = new ByteArrayResource(reply.get().value());

            return ResponseEntity.ok()
                    .headers(this.headers(body.getCategory() + "_" + body.getName()))
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.parseMediaType
                            ("application/octet-stream")).body(resource);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private HttpHeaders headers(String fileName) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName + ".pdf");
        header.add("Cache-Control", "no-cache, no-store,"
                + " must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }
}
