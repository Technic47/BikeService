package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.modelServices.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

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

    public List<AbstractShowableEntity> doGlobalSearchProcedure(
            String findBy, String searchValue, final UserModel userModel, boolean shared) {

        List<AbstractShowableEntity> results = new ArrayList<>();

        switch (findBy) {
            case "name" -> results.addAll(findAllByNameOrShared(searchValue, userModel, shared));
            case "description" -> results.addAll(findAllByDescriptionOrShared(searchValue, userModel, shared));
            case "value" -> results.addAll(findAllByValueOrShared(searchValue, userModel, shared));
        }
        return results;
    }

    private List<AbstractShowableEntity> findAllByNameOrShared(String searchValue, final UserModel userModel, boolean shared){
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

    private List<AbstractShowableEntity> findAllByDescriptionOrShared(String searchValue, final UserModel userModel, boolean shared){
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

    private List<AbstractShowableEntity> findAllByValueOrShared(String searchValue, final UserModel userModel, boolean shared){
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
}
