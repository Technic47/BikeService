package ru.kuznetsov.controllersrest.rest.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.controllers.abstracts.AbstractEntityController;
import ru.bikeservice.mainresources.customExceptions.ResourceNotFoundException;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.models.users.UserModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/admin/items")
public class ItemsControllerRESTAdmin extends AbstractEntityController {

    @GetMapping("/createdBy/{userId}")
    public ResponseEntity getCreatedItems(@PathVariable Long userId) {
        if (userService.existById(userId)) {
            Map<Object, Object> response = new HashMap<>();
            UserModel userModel = userService.getById(userId);

//            response.put("documents", mainExecutor.submit(() -> documentService.findByCreator(userId)).get());
//            response.put("fasteners", mainExecutor.submit(() -> fastenerService.findByCreator(userId)).get());
//            response.put("manufacturers", mainExecutor.submit(() -> manufacturerService.findByCreator(userId)).get());
//            response.put("consumables", mainExecutor.submit(() -> consumableService.findByCreator(userId)).get());
//            response.put("tools", mainExecutor.submit(() -> toolService.findByCreator(userId)).get());
//            response.put("parts", mainExecutor.submit(() -> partService.findByCreator(userId)).get());
//            response.put("bikes", mainExecutor.submit(() -> bikeService.findByCreator(userId)).get());

            response.put("documents", mainExecutor.submit(() -> doIndexProcedure(userModel, Document.class.getSimpleName(), false)));
            response.put("fasteners", mainExecutor.submit(() -> doIndexProcedure(userModel, Fastener.class.getSimpleName(), false)));
            response.put("manufacturers", mainExecutor.submit(() -> doIndexProcedure(userModel, Manufacturer.class.getSimpleName(), false)));
            response.put("consumables", mainExecutor.submit(() -> doIndexProcedure(userModel, Consumable.class.getSimpleName(), false)));
            response.put("tools", mainExecutor.submit(() -> doIndexProcedure(userModel, Tool.class.getSimpleName(), false)));
            response.put("parts", mainExecutor.submit(() -> doIndexProcedure(userModel, Part.class.getSimpleName(), false)));
            response.put("bikes", mainExecutor.submit(() -> doIndexProcedure(userModel, Bike.class.getSimpleName(), false)));

            return ResponseEntity.ok(response);
        } else throw new ResourceNotFoundException(userId);
    }

    @GetMapping("/documents")
    public List<Document> getDocumentsList() throws ExecutionException, InterruptedException {
        Future<List<Document>> allDocuments = mainExecutor.submit(() -> doIndexProcedure(Document.class.getSimpleName()));
        return allDocuments.get();
    }

    @GetMapping("/fasteners")
    public List<Fastener> getFastenersList() throws ExecutionException, InterruptedException {
        Future<List<Fastener>> allFasteners = mainExecutor.submit(() -> doIndexProcedure(Fastener.class.getSimpleName()));
        return allFasteners.get();
    }

    @GetMapping("/manufacturers")
    public List<Manufacturer> getManufacturersList() throws ExecutionException, InterruptedException {
        Future<List<Manufacturer>> allManufacturers = mainExecutor.submit(() -> doIndexProcedure(Manufacturer.class.getSimpleName()));
        return allManufacturers.get();
    }

    @GetMapping("/consumables")
    public List<Consumable> getConsumablesList() throws ExecutionException, InterruptedException {
        Future<List<Consumable>> allConsumables = mainExecutor.submit(() -> doIndexProcedure(Consumable.class.getSimpleName()));
        return allConsumables.get();
    }

    @GetMapping("/tools")
    public List<Tool> getToolsList() throws ExecutionException, InterruptedException {
        Future<List<Tool>> allTools = mainExecutor.submit(() -> doIndexProcedure(Tool.class.getSimpleName()));
        return allTools.get();
    }

    @GetMapping("/parts")
    public List<Part> getPartsList() throws ExecutionException, InterruptedException {
        Future<List<Part>> allParts = mainExecutor.submit(() -> doIndexProcedure(Part.class.getSimpleName()));
        return allParts.get();
    }

    @GetMapping("/bikes")
    public List<Bike> getBikesList() throws ExecutionException, InterruptedException {
        Future<List<Bike>> allBikes = mainExecutor.submit(() -> doIndexProcedure(Bike.class.getSimpleName()));
        return allBikes.get();
    }

    @GetMapping("/documents/{id}")
    public Document getDocument(@PathVariable Long id) {
        return doShowProcedure(Document.class.getSimpleName(), id);
    }

    @GetMapping("/fasteners/{id}")
    public Fastener getFastener(@PathVariable Long id) {
        return doShowProcedure(Fastener.class.getSimpleName(), id);
    }

    @GetMapping("/manufacturers/{id}")
    public Manufacturer getManufacturer(@PathVariable Long id) {
        return doShowProcedure(Manufacturer.class.getSimpleName(), id);
    }

    @GetMapping("/consumables/{id}")
    public Consumable getConsumable(@PathVariable Long id) {
        return doShowProcedure(Consumable.class.getSimpleName(), id);
    }

    @GetMapping("/tools/{id}")
    public Tool getTool(@PathVariable Long id) {
        return doShowProcedure(Tool.class.getSimpleName(), id);
    }

    @GetMapping("/parts/{id}")
    public Part getPart(@PathVariable Long id) {
        return doShowProcedure(Part.class.getSimpleName(), id);
    }

    @GetMapping("/bikes/{id}")
    public Bike getBike(@PathVariable Long id) {
        return doShowProcedure(Bike.class.getSimpleName(), id);
    }
}
