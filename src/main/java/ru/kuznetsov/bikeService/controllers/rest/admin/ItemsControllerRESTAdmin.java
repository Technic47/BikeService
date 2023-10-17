package ru.kuznetsov.bikeService.controllers.rest.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.customExceptions.ResourceNotFoundException;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.services.modelServices.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/admin/items")
public class ItemsControllerRESTAdmin extends AbstractEntityController {
    private final DocumentService documentService;
    private final FastenerService fastenerService;
    private final ConsumableService consumableService;
    private final ManufacturerService manufacturerService;
    private final ToolService toolService;
    private final PartService partService;
    private final BikeService bikeService;

    public ItemsControllerRESTAdmin(DocumentService documentService, FastenerService fastenerService, ConsumableService consumableService, ManufacturerService manufacturerService, ToolService toolService, PartService partService, BikeService bikeService) {
        this.documentService = documentService;
        this.fastenerService = fastenerService;
        this.consumableService = consumableService;
        this.manufacturerService = manufacturerService;
        this.toolService = toolService;
        this.partService = partService;
        this.bikeService = bikeService;
    }

    @GetMapping("/createdBy/{userId}")
    public ResponseEntity getCreatedItems(@PathVariable Long userId) throws ExecutionException, InterruptedException {
        if (userService.existById(userId)) {
            Map<Object, Object> response = new HashMap<>();

            response.put("documents", mainExecutor.submit(() -> documentService.findByCreator(userId)).get());
            response.put("fasteners", mainExecutor.submit(() -> fastenerService.findByCreator(userId)).get());
            response.put("manufacturers", mainExecutor.submit(() -> manufacturerService.findByCreator(userId)).get());
            response.put("consumables", mainExecutor.submit(() -> consumableService.findByCreator(userId)).get());
            response.put("tools", mainExecutor.submit(() -> toolService.findByCreator(userId)).get());
            response.put("parts", mainExecutor.submit(() -> partService.findByCreator(userId)).get());
            response.put("bikes", mainExecutor.submit(() -> bikeService.findByCreator(userId)).get());

            return ResponseEntity.ok(response);
        } else throw new ResourceNotFoundException(userId);
    }

    @GetMapping("/documents")
    public List<Document> getDocumentsList() throws ExecutionException, InterruptedException {
        Future<List<Document>> allDocuments = mainExecutor.submit(documentService::index);
        return allDocuments.get();
    }

    @GetMapping("/fasteners")
    public List<Fastener> getFastenersList() throws ExecutionException, InterruptedException {
        Future<List<Fastener>> allFasteners = mainExecutor.submit(fastenerService::index);
        return allFasteners.get();
    }

    @GetMapping("/manufacturers")
    public List<Manufacturer> getManufacturersList() throws ExecutionException, InterruptedException {
        Future<List<Manufacturer>> allManufacturers = mainExecutor.submit(manufacturerService::index);
        return allManufacturers.get();
    }

    @GetMapping("/consumables")
    public List<Consumable> getConsumablesList() throws ExecutionException, InterruptedException {
        Future<List<Consumable>> allConsumables = mainExecutor.submit(consumableService::index);
        return allConsumables.get();
    }

    @GetMapping("/tools")
    public List<Tool> getToolsList() throws ExecutionException, InterruptedException {
        Future<List<Tool>> allTools = mainExecutor.submit(toolService::index);
        return allTools.get();
    }

    @GetMapping("/parts")
    public List<Part> getPartsList() throws ExecutionException, InterruptedException {
        Future<List<Part>> allParts = mainExecutor.submit(partService::index);
        return allParts.get();
    }

    @GetMapping("/bikes")
    public List<Bike> getBikesList() throws ExecutionException, InterruptedException {
        Future<List<Bike>> allBikes = mainExecutor.submit(bikeService::index);
        return allBikes.get();
    }

    @GetMapping("/documents/{id}")
    public Document getDocument(@PathVariable Long id) {
        return documentService.getById(id);
    }

    @GetMapping("/fasteners/{id}")
    public Fastener getFastener(@PathVariable Long id) {
        return fastenerService.getById(id);
    }

    @GetMapping("/manufacturers/{id}")
    public Manufacturer getManufacturer(@PathVariable Long id) {
        return manufacturerService.getById(id);
    }

    @GetMapping("/consumables/{id}")
    public Consumable getConsumable(@PathVariable Long id) {
        return consumableService.getById(id);
    }

    @GetMapping("/tools/{id}")
    public Tool getTool(@PathVariable Long id) {
        return toolService.getById(id);
    }

    @GetMapping("/parts/{id}")
    public Part getPart(@PathVariable Long id) {
        return partService.getById(id);
    }

    @GetMapping("/bikes/{id}")
    public Bike getBike(@PathVariable Long id) {
        return bikeService.getById(id);
    }
}
