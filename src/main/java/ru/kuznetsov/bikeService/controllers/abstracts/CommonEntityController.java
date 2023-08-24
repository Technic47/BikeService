package ru.kuznetsov.bikeService.controllers.abstracts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;
import ru.kuznetsov.bikeService.services.modelServices.BikeService;
import ru.kuznetsov.bikeService.services.modelServices.PartService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;
import static ru.kuznetsov.bikeService.services.PDFService.PDF_DOC_NAME;

public abstract class CommonEntityController extends AbstractController {
    protected PartService partService;
    protected BikeService bikeService;
    protected PDFService pdfService;

    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>> List<T> buildIndexListIncludeShared(
            final S service, UserModel userModel, String category) {
        List<T> objects = null;

        if (userModel.getAuthorities().contains(ROLE_USER)) {
            objects = service.findByCreatorOrShared(userModel.getId());
            logger.info("personal " + category + " are shown to '" + userModel.getUsername() + "'");
        }
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            objects = service.index();
            logger.info(category + " are shown to " + userModel.getUsername());
        }
        return objects;
    }

    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>> List<T> buildIndexListExcludeShared(
            final S service, UserModel userModel, String category) {
        List<T> objects = null;

        if (userModel.getAuthorities().contains(ROLE_USER)) {
            objects = service.findByCreator(userModel.getId());
            logger.info("personal " + category + " are shown to '" + userModel.getUsername() + "'");
        }
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            objects = service.index();
            logger.info(category + " are shown to " + userModel.getUsername());
        }
        return objects;
    }

    protected <T extends AbstractShowableEntity> void addIndexMapsToModel(Model model, Long userId, List<T> objects) {
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

    protected <T extends AbstractShowableEntity> List<AbstractEntityDto> convertItemsToDto(List<T> objects) {
        List<AbstractEntityDto> indexList = new ArrayList<>();
        if (objects != null) {
            objects.forEach(object -> {
                indexList.add(new AbstractEntityDto(object));
            });
        }
        return indexList;
    }

    protected <T extends AbstractShowableEntity> void addIndexListsToResponse(Map<Object, Object> response, Long userId, List<T> objects) {
        List<AbstractEntityDto> indexList = new ArrayList<>();
        List<AbstractEntityDto> sharedIndexList = new ArrayList<>();
        if (objects != null) {
            objects.forEach(object -> {
                if (object.getCreator().equals(userId)) {
                    indexList.add(new AbstractEntityDto(object));
                } else sharedIndexList.add(new AbstractEntityDto(object));
            });
        }
        response.put("indexList", indexList);
        response.put("sharedIndexList", sharedIndexList);
    }

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
        logger.info(item + " was created by '" + userModel.getUsername());
        return createdItem;
    }

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


    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>>
    void doDeleteProcedure(final T item, final S service, Principal principal, String category) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Long itemId = item.getId();
        service.delete(itemId);
        this.cleanUpAfterDelete(item, itemId, userModel);
        logger.info(category +
                " id:" + itemId + " was deleted by '" + userModel.getUsername() + "'");
    }

    protected <T extends AbstractShowableEntity> void cleanUpAfterDelete(T item, Long id, UserModel userModel) {
        String partType = item.getClass().getSimpleName();
        Runnable clearUser = () -> userService
                .delCreatedItem(userModel, new UserEntity(item.getClass().getSimpleName(), id));
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

    protected <T extends AbstractShowableEntity> boolean checkAccessToItem(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            return true;
        } else {
            return item.getCreator().equals(userModel.getId());
        }
    }

    protected <T extends AbstractShowableEntity> void checkImageFile(MultipartFile file, T item) {
        if (file != null) {
            if (!file.isEmpty()) {
                Picture picture = pictureService.save(file);
                item.setPicture(picture.getId());
            }
        }
    }

    protected <T extends AbstractShowableEntity> ResponseEntity<Resource> prepareResponse(T item, Principal principal) throws IOException {
//        this.preparePDF(item, principal);
        return this.createResponse(item);
    }

    protected <T extends AbstractShowableEntity> ResponseEntity<Resource> createResponse(T item) throws IOException {
        File file = new File(PDF_DOC_NAME);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource
                (Files.readAllBytes(path));

        return ResponseEntity.ok().headers(this.headers(item.getClass().getSimpleName() + "_" + item.getId()))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType
                        ("application/octet-stream")).body(resource);
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
    public void setPdfService(PDFService pdfService) {
        this.pdfService = pdfService;
    }

    @Autowired
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    @Autowired
    public void setBikeService(BikeService bikeService) {
        this.bikeService = bikeService;
    }
}
