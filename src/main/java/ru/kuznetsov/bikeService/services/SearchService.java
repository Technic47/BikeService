package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;
import ru.kuznetsov.bikeService.services.modelServices.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;

@Service
public class SearchService {
    protected final DocumentService documentService;
    protected final FastenerService fastenerService;
    protected final ManufacturerService manufacturerService;
    protected final ConsumableService consumableService;
    protected final ToolService toolService;
    protected final PartService partService;
    protected final BikeService bikeService;
    protected final ExecutorService mainExecutor;
    protected final ExecutorService additionExecutor;

    public SearchService(DocumentService documentService, FastenerService fastenerService, ManufacturerService manufacturerService, ConsumableService consumableService, ToolService toolService, PartService partService, BikeService bikeService, @Qualifier("MainExecutor") ExecutorService mainExecutor, @Qualifier("AdditionExecutor") ExecutorService additionExecutor) {
        this.documentService = documentService;
        this.fastenerService = fastenerService;
        this.manufacturerService = manufacturerService;
        this.consumableService = consumableService;
        this.toolService = toolService;
        this.partService = partService;
        this.bikeService = bikeService;
        this.mainExecutor = mainExecutor;
        this.additionExecutor = additionExecutor;
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


    public List<AbstractShowableEntity> doGlobalSearchProcedure(
            String findBy, String searchValue, final UserModel userModel, boolean shared) {

        List<AbstractShowableEntity> results = new ArrayList<>();

        switch (findBy) {
            case "name" -> results.addAll(findAllByNameOrShared(searchValue, userModel, shared));
            case "description" -> results.addAll(findAllByDescriptionOrShared(searchValue, userModel, shared));
            case "value" -> results.addAll(findAllByValueOrShared(searchValue, userModel, shared));
            case "manufacturer" -> results.addAll(findAllByManufacturer(searchValue, userModel, shared));
            case "model" -> results.addAll(findAllByModelOrShared(searchValue, userModel, shared));
            default -> {
                results.addAll(findAllByNameOrShared(searchValue, userModel, shared));
                results.addAll(findAllByDescriptionOrShared(searchValue, userModel, shared));
            }
        }
        return results;
    }

    private List<AbstractShowableEntity> findAllByNameOrShared(final String searchValue, final UserModel userModel, boolean shared) {
        List<AbstractShowableEntity> results = new ArrayList<>();
        results.addAll(findByNameCreatorShared(searchValue, "documents", userModel, shared));
        results.addAll(findByNameCreatorShared(searchValue, "fasteners", userModel, shared));
        results.addAll(findByNameCreatorShared(searchValue, "manufacturers", userModel, shared));
        results.addAll(findByNameCreatorShared(searchValue, "consumables", userModel, shared));
        results.addAll(findByNameCreatorShared(searchValue, "tools", userModel, shared));
        results.addAll(findByNameCreatorShared(searchValue, "parts", userModel, shared));
        results.addAll(findByNameCreatorShared(searchValue, "bikes", userModel, shared));

        return results;
    }

    private List<AbstractShowableEntity> findAllByDescriptionOrShared(final String searchValue, final UserModel userModel, boolean shared) {
        List<AbstractShowableEntity> results = new ArrayList<>();
        results.addAll(findByDescriptionCreatorShared(searchValue, "documents", userModel, shared));
        results.addAll(findByDescriptionCreatorShared(searchValue, "fasteners", userModel, shared));
        results.addAll(findByDescriptionCreatorShared(searchValue, "manufacturers", userModel, shared));
        results.addAll(findByDescriptionCreatorShared(searchValue, "consumables", userModel, shared));
        results.addAll(findByDescriptionCreatorShared(searchValue, "tools", userModel, shared));
        results.addAll(findByDescriptionCreatorShared(searchValue, "parts", userModel, shared));
        results.addAll(findByDescriptionCreatorShared(searchValue, "bikes", userModel, shared));

        return results;
    }

    private List<AbstractShowableEntity> findAllByValueOrShared(final String searchValue, final UserModel userModel, boolean shared) {
        List<AbstractShowableEntity> results = new ArrayList<>();
        results.addAll(findByValueCreatorShared(searchValue, "documents", userModel, shared));
        results.addAll(findByValueCreatorShared(searchValue, "fasteners", userModel, shared));
        results.addAll(findByValueCreatorShared(searchValue, "manufacturers", userModel, shared));
        results.addAll(findByValueCreatorShared(searchValue, "consumables", userModel, shared));
        results.addAll(findByValueCreatorShared(searchValue, "tools", userModel, shared));
        results.addAll(findByValueCreatorShared(searchValue, "parts", userModel, shared));
        results.addAll(findByValueCreatorShared(searchValue, "bikes", userModel, shared));

        return results;
    }

    private List<AbstractShowableEntity> findAllByManufacturer(final String searchValue, final UserModel userModel, boolean shared) {
        List<AbstractShowableEntity> results = new ArrayList<>();
        results.addAll(findByManufactureCreatorShared(searchValue, "consumables", userModel, shared));
        results.addAll(findByManufactureCreatorShared(searchValue, "tools", userModel, shared));
        results.addAll(findByManufactureCreatorShared(searchValue, "parts", userModel, shared));
        results.addAll(findByManufactureCreatorShared(searchValue, "bikes", userModel, shared));

        return results;
    }

    private List<AbstractShowableEntity> findAllByModelOrShared(final String searchValue, final UserModel userModel, boolean shared) {
        List<AbstractShowableEntity> results = new ArrayList<>();
        results.addAll(findByModelCreatorShared(searchValue, "consumables", userModel, shared));
        results.addAll(findByModelCreatorShared(searchValue, "tools", userModel, shared));
        results.addAll(findByModelCreatorShared(searchValue, "parts", userModel, shared));
        results.addAll(findByModelCreatorShared(searchValue, "bikes", userModel, shared));

        return results;
    }

    private List<AbstractShowableEntity> findByNameCreatorShared(String searchValue, String category, UserModel userModel, boolean shared) {
        List<AbstractShowableEntity> results = new ArrayList<>();

        switch (category) {
            case "documents" ->
                    results.addAll(documentService.findByNameContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "fasteners" ->
                    results.addAll(fastenerService.findByNameContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "manufacturers" ->
                    results.addAll(manufacturerService.findByNameContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "consumables" ->
                    results.addAll(consumableService.findByNameContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "tools" ->
                    results.addAll(toolService.findByNameContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "parts" ->
                    results.addAll(partService.findByNameContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "bikes" ->
                    results.addAll(bikeService.findByNameContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            default -> throw new IllegalArgumentException("Argument " + category + " is wrong!");
        }

        return results;
    }

    private List<AbstractShowableEntity> findByDescriptionCreatorShared(String searchValue, String category, UserModel userModel, boolean shared) {
        List<AbstractShowableEntity> results = new ArrayList<>();

        switch (category) {
            case "documents" ->
                    results.addAll(documentService.findByDescriptionContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "fasteners" ->
                    results.addAll(fastenerService.findByDescriptionContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "manufacturers" ->
                    results.addAll(manufacturerService.findByDescriptionContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "consumables" ->
                    results.addAll(consumableService.findByDescriptionContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "tools" ->
                    results.addAll(toolService.findByDescriptionContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "parts" ->
                    results.addAll(partService.findByDescriptionContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "bikes" ->
                    results.addAll(bikeService.findByDescriptionContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            default -> throw new IllegalArgumentException("Argument " + category + " is wrong!");
        }

        return results;
    }

    private List<AbstractShowableEntity> findByValueCreatorShared(String searchValue, String category, UserModel userModel, boolean shared) {
        List<AbstractShowableEntity> results = new ArrayList<>();

        switch (category) {
            case "documents" ->
                    results.addAll(documentService.findByValueContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "fasteners" ->
                    results.addAll(fastenerService.findByValueContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "manufacturers" ->
                    results.addAll(manufacturerService.findByValueContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "consumables" ->
                    results.addAll(consumableService.findByValueContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "tools" ->
                    results.addAll(toolService.findByValueContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "parts" ->
                    results.addAll(partService.findByValueContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "bikes" ->
                    results.addAll(bikeService.findByValueContainingIgnoreCaseAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            default -> throw new IllegalArgumentException("Argument " + category + " is wrong!");
        }

        return results;
    }

    private List<AbstractUsableEntity> findByManufactureCreatorShared(String searchValue, String category, UserModel userModel, boolean shared) {
        List<AbstractUsableEntity> results = new ArrayList<>();
        List<Manufacturer> manufacturers = manufacturerService.findByNameContainingIgnoreCase(searchValue);

        if (manufacturers.isEmpty()) {
            throw new IllegalArgumentException("Manufacturer with name: " + searchValue + " is not found!");
        }

        manufacturers.forEach(item -> {
            switch (category) {
                case "consumables" ->
                        results.addAll(consumableService.findByManufacturerAndCreatorOrIsShared(item.getId(), userModel.getId(), shared));
                case "tools" ->
                        results.addAll(toolService.findByManufacturerAndCreatorOrIsShared(item.getId(), userModel.getId(), shared));
                case "parts" ->
                        results.addAll(partService.findByManufacturerAndCreatorOrIsShared(item.getId(), userModel.getId(), shared));
                case "bikes" ->
                        results.addAll(bikeService.findByManufacturerAndCreatorOrIsShared(item.getId(), userModel.getId(), shared));
                default -> throw new IllegalArgumentException("Argument " + category + " is wrong!");
            }
        });

        return results;
    }

    private List<AbstractUsableEntity> findByModelCreatorShared(String searchValue, String category, UserModel userModel, boolean shared) {
        List<AbstractUsableEntity> results = new ArrayList<>();

        switch (category) {
            case "consumables" ->
                    results.addAll(consumableService.findByModelAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "tools" ->
                    results.addAll(toolService.findByModelAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "parts" ->
                    results.addAll(partService.findByModelAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            case "bikes" ->
                    results.addAll(bikeService.findByModelAndCreatorOrIsShared(searchValue, userModel.getId(), shared));
            default -> throw new IllegalArgumentException("Argument " + category + " is wrong!");
        }

        return results;
    }
}
