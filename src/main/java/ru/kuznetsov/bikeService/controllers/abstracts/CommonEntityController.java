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
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.SearchService;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.services.PDFService.PDF_DOC_NAME;

/**
 * Intermediate layer that includes common methods for REST and non-REST controllers.
 */
public abstract class CommonEntityController extends AbstractController {
    protected PDFService pdfService;
    protected SearchService searchService;

    /**
     * Form and add Maps with owned and created entities. Add Maps to provided Model object.
     *
     * @param model   Model object where to put formed Maps.
     * @param userId  Id of user. User to for creation check in filtering results.
     * @param objects List of objects.
     * @param <T>     AbstractShowableEntity from main models.
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
     * Main procedure for creation new entity.
     *
     * @param item      entity with new information.
     * @param service   connected to entity service.
     * @param file      Multipart file with picture.
     * @param principal principal who is updating.
     * @param <T>       AbstractShowableEntity from main models.
     * @param <S>
     * @return updated entity.
     */
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
     * @param <T>       AbstractShowableEntity from main models.
     * @param <S>
     * @return updated entity.
     */
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
     * @param <T>       AbstractShowableEntity from main models.
     * @param <S>
     */
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
     * @param <T>       AbstractShowableEntity from main models.
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
     * Searching via matching in Name and Description. Case is ignored. ResultSet is formed considering current user`s ROLE.
     * If user is ADMIN -> no filtering.
     * Else -> filtering remains shared and ID-filtered items, or just ID-filtered items.
     *
     * @param findBy      string field to search.
     * @param searchValue string to search.
     * @param service     connected to entity service.
     * @param principal   who is searching.
     * @param shared      flag for including shared items to resultSet.
     * @param category    category of entities for logging.
     * @param <T>         AbstractShowableEntity from main models.
     * @param <S>
     * @return List with search results.
     */
    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>> List<T> doSearchProcedure(
            String findBy, String searchValue, final S service, final Principal principal, boolean shared, String category) {
        List<T> resultSet;
        UserModel userModel = this.getUserModelFromPrincipal(principal);

        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            resultSet = this.getAdminResults(findBy, searchValue, service, principal, category);
        } else resultSet = this.getUserResults(findBy, searchValue, service, principal, shared, category);

        logger.info(userModel.getUsername() + " was searching " + searchValue + " in " + category);
        return resultSet;
    }

    private <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>> List<T> getAdminResults(
            String findBy, String searchValue, final S service, final Principal principal, String category
    ) {
        Set<T> resultSet = new HashSet<>();

        switch (findBy) {
            case "name" -> resultSet.addAll(service.findByNameContainingIgnoreCase(searchValue));
            case "description" -> resultSet.addAll(service.findByDescriptionContainingIgnoreCase(searchValue));
            case "value" -> resultSet.addAll(service.findByValueContainingIgnoreCase(searchValue));
            default -> {
                resultSet.addAll(service.findByNameContainingIgnoreCase(searchValue));
                resultSet.addAll(service.findByDescriptionContainingIgnoreCase(searchValue));
            }
        }

        UserModel userModel = this.getUserModelFromPrincipal(principal);

        logger.info(userModel.getUsername() + " was searching " + searchValue + " in " + category);
        return resultSet.stream().toList();
    }

    protected <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>> List<T> getUserResults(
            String findBy, String searchValue, final S service, final Principal principal, boolean shared, String category) {
        Set<T> resultSet = new HashSet<>();
        UserModel userModel = this.getUserModelFromPrincipal(principal);

        switch (findBy) {
            case "name" ->
                    resultSet.addAll(service.findByNameContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "description" -> resultSet.addAll(service.findByDescriptionContainingIgnoreCase(searchValue));
            case "value" -> resultSet.addAll(service.findByValueContainingIgnoreCase(searchValue));
            default -> {
                resultSet.addAll(service.findByNameContainingIgnoreCase(searchValue));
                resultSet.addAll(service.findByDescriptionContainingIgnoreCase(searchValue));
            }
        }


        Long userId = userModel.getId();

        if (!userModel.getAuthorities().contains(ROLE_ADMIN)) {
            if (shared) {
                resultSet = resultSet.stream()
                        .filter(item -> item.getCreator().equals(userId)
                                || item.getIsShared())
                        .collect(Collectors.toSet());
            } else {
                resultSet = resultSet.stream()
                        .filter(item -> item.getCreator().equals(userId))
                        .collect(Collectors.toSet());
            }
        }
        logger.info(userModel.getUsername() + " was searching " + searchValue + " in " + category);
        return resultSet.stream().toList();
    }

    /**
     * Checking access of Principal to specified entity.
     *
     * @param item      secured entity.
     * @param principal Principal to check.
     * @param <T>       AbstractShowableEntity from main models.
     * @return access boolean.
     */
    protected <T extends AbstractShowableEntity> boolean checkAccessToItem(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            this.logAccess(item.toString(), userModel.getUsername(), true);
            return true;
        } else {
            boolean access = item.getCreator().equals(userModel.getId());
            this.logAccess(item.toString(), userModel.getUsername(), access);
            return access;
        }
    }

    private void logAccess(String item, String userName, boolean access) {
        logger.info("User " + userName + " tries to get access to item: " + item + " - " + access);
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
    private void setPdfService(PDFService pdfService) {
        this.pdfService = pdfService;
    }

    @Autowired
    private void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }
}
