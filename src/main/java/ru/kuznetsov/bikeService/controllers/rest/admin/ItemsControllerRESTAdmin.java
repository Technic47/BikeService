package ru.kuznetsov.bikeService.controllers.rest.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.abstracts.CommonEntityController;
import ru.kuznetsov.bikeService.exceptionHandlers.ResourceNotFoundException;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.services.modelServices.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/items")
public class ItemsControllerRESTAdmin extends CommonEntityController {
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
    public ResponseEntity getCreatedItems(@PathVariable Long userId) {
        if (userService.existById(userId)) {
            Map<Object, Object> response = new HashMap<>();
            response.put("documents", documentService.findByCreator(userId));
            response.put("fasteners", fastenerService.findByCreator(userId));
            response.put("manufacturers", manufacturerService.findByCreator(userId));
            response.put("consumables", consumableService.findByCreator(userId));
            response.put("tools", toolService.findByCreator(userId));
            response.put("parts", partService.findByCreator(userId));
            response.put("bikes", bikeService.findByCreator(userId));
            return ResponseEntity.ok(response);
        } else throw new ResourceNotFoundException(userId);
    }

    @GetMapping("/documents")
    public List<Document> getDocumentsList() {
        return documentService.index();
    }

    @GetMapping("/fasteners")
    public List<Fastener> getFastenersList() {
        return fastenerService.index();
    }

    @GetMapping("/manufacturers")
    public List<Manufacturer> getManufacturersList() {
        return manufacturerService.index();
    }

    @GetMapping("/consumables")
    public List<Consumable> getConsumablesList() {
        return consumableService.index();
    }

    @GetMapping("/tools")
    public List<Tool> getToolsList() {
        return toolService.index();
    }

    @GetMapping("/parts")
    public List<Part> getPartsList() {
        return partService.index();
    }

    @GetMapping("/bikes")
    public List<Bike> getBikesList() {
        return bikeService.index();
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
