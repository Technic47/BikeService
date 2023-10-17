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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.abstracts.AbstractUsableEntity;
import ru.bikeservice.mainresources.models.dto.PdfEntityDto;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.lists.ServiceList;
import ru.bikeservice.mainresources.models.lists.UserEntity;
import ru.bikeservice.mainresources.models.pictures.Picture;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.bikeservice.mainresources.services.SearchService;
import ru.bikeservice.mainresources.services.abstracts.CommonAbstractEntityService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.bikeservice.mainresources.models.users.UserRole.ROLE_ADMIN;
import static ru.bikeservice.mainresources.models.users.UserRole.ROLE_USER;

/**
 * Intermediate layer that includes common methods for REST and non-REST controllers.
 */
public abstract class AbstractEntityController extends AbstractController {
    protected SearchService searchService;
    @Autowired
    private ReplyingKafkaTemplate<String, PdfEntityDto, byte[]> pdfKafkaTemplate;


    /**
     * Creates List of entities depending on userModel role and shared flag.
     *
     * @param service   service for entities.
     * @param userModel user for whom List is being created.
     * @param category  category of entities for logging.
     * @param shared    flag for including shared entities.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     * @param <S>
     * @return formed List.
     */
    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>> List<T> doIndexProcedure(
            final S service, UserModel userModel, String category, boolean shared) {
        List<T> objects = null;

        if (userModel.getAuthorities().contains(ROLE_USER)) {
            if (shared) {
                objects = service.findByCreatorOrShared(userModel.getId());
                logger.info("Personal and shared " + category + " are shown to '" + userModel.getUsername() + "'");
            } else {
                objects = service.findByCreator(userModel.getId());
                logger.info("Personal " + category + " are shown to '" + userModel.getUsername() + "'");
            }
        }
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            objects = service.index();
            logger.info("All " + category + " are shown to '" + userModel.getUsername() + "'");
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
            Model model, Long userId, List<T> objects) {
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
     * @param item      entity with new information.
     * @param principal principal to whom entity is shown.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     * @param <S>
     * @return entity.
     */
    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>> T doShowProcedure(
            final T item, Principal principal
    ) {
        logger.info(item.getClass()
                .getSimpleName() + " " + item.getId() + " was shown to '" + this.getUserModelFromPrincipal(principal).getUsername() + "'");
        return item;
    }

    /**
     * Main procedure for creation new entity.
     *
     * @param item      entity with new information.
     * @param service   connected to entity service.
     * @param file      Multipart file with picture.
     * @param principal principal who is updating.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     * @param <S>
     * @return updated entity.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>>
    T doCreateProcedure(final T item, final S service, MultipartFile file, Principal principal) {
        this.checkImageFile(file, item);
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        item.setCreator(userModel.getId());
        item.setCreated(new Date());
        T createdItem = service.save(item);
        userService.addCreatedItem(userModel,
                new UserEntity(item.getClass().getSimpleName(), createdItem.getId()));
        logger.info(item.getId() + ":" + item.getName() + " was created by '" + userModel.getUsername());
        return createdItem;
    }

    /**
     * Main procedure for updating entity.
     *
     * @param newItem   entity with new information.
     * @param service   connected to entity service.
     * @param oldItem   entity to apply new information.
     * @param file      Multipart file with picture.
     * @param principal principal who is updating.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     * @param <S>
     * @return updated entity.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>>
    T doUpdateProcedure(final T newItem, final S service, final T oldItem, MultipartFile file, Principal principal) {
        this.checkImageFile(file, newItem);
        T updated = service.update(oldItem, newItem);
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        logger.info(newItem.getClass()
                .getSimpleName() + " id:" + oldItem.getId() + " was edited by '" + userModel.getUsername() + "'");
        return updated;
    }

    /**
     * Main procedure for deleting entity. Cleans up connected tables.
     *
     * @param item      entity for deleting.
     * @param service   connected to entity service.
     * @param principal principal who is trying to delete the item.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     * @param <S>
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = {SQLException.class, RuntimeException.class})
    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>>
    void doDeleteProcedure(final T item, final S service, final Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Long itemId = item.getId();
        service.delete(itemId);
        this.cleanUpAfterDelete(item, userModel);
        String category = item.getClass().getSimpleName();
        logger.info(category +
                " id:" + itemId + " was deleted by '" + userModel.getUsername() + "'");
    }

    /**
     * Deletes entity record from user createdList.
     * Checks if entity is linked to any Part or Bike and deletes it from linkedItems.
     *
     * @param item      entity to delete.
     * @param userModel creator of entity.
     * @param <T>       AbstractShowableEntity from main mainResources.models.
     */
    private <T extends AbstractShowableEntity> void cleanUpAfterDelete(
            T item, UserModel userModel) {
        String partType = item.getClass().getSimpleName();
        Long id = item.getId();
        Runnable clearUser = () -> userService
                .delCreatedItem(userModel, new UserEntity(item));
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
        mainExecutor.submit(clearUser);
        mainExecutor.submit(clearParts);
        mainExecutor.submit(clearBikes);
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
     * @param <T>  AbstractShowableEntity from main mainResources.models.
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

    protected ResponseEntity<Resource> preparePDF(PdfEntityDto body) {
        ProducerRecord<String, PdfEntityDto> record = new ProducerRecord<>("pdf", body);
        RequestReplyFuture<String, PdfEntityDto, byte[]> reply = pdfKafkaTemplate.sendAndReceive(record);

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


    @Autowired
    private void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }
}
