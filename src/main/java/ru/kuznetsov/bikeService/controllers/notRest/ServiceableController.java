package ru.kuznetsov.bikeService.controllers.notRest;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.lists.ServiceList;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.services.abstracts.CommonServiceableEntityService;
import ru.kuznetsov.bikeService.controllers.ServiceListController;

import java.io.IOException;
import java.security.Principal;

@Component
public abstract class ServiceableController<T extends AbstractServiceableEntity,
        S extends CommonServiceableEntityService<T>>
        extends UsableController<T, S> {
    private ServiceListController serviceListController;

    public ServiceableController(S service) {
        super(service);
    }

    @Operation(hidden = true)
    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Model model,
                       Principal principal) {
        T item = service.getById(id);
        if (item == null) {
            return "redirect:/" + category;
        }
        ServiceList serviceList = serviceListController.getServiceList(item.getLinkedItems());
        Long manufactureIndex = item.getManufacturer();
        model.addAttribute("manufacture", manufacturerService.getById(manufactureIndex).getName());
        this.addLinkedItemsToModel(model, serviceList);
        this.addItemAttributesShow(model, item, principal);
        return super.show(item, model, principal, category);
    }

    @Operation(hidden = true)
    @Override
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id, Principal principal) {
        T item = service.getById(id);
        if (item == null) {
            return "redirect:/" + category;
        }
        return this.editWithItem(model, item, principal);
    }

    private String editWithItem(Model model, T item, Principal principal) {
        ServiceList serviceList = serviceListController.getServiceList(item.getLinkedItems());
        this.addAllItemsToModel(model);
        this.addLinkedItemsToModel(model, serviceList);
        return super.edit(model, item, principal);
    }

    @Operation(hidden = true)
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public String updateServiceList(
            @Valid @ModelAttribute("object") T item,
            BindingResult bindingResult,
            @PathVariable("id") Long id,
            @RequestParam(value = "action") String action,
            @RequestParam(value = "documentId", required = false) Long documentId,
            @RequestParam(value = "fastenerId", required = false) Long fastenerId,
            @RequestParam(value = "fastenerQuantity", required = false) Integer fastenerQuantity,
            @RequestParam(value = "toolId", required = false) Long toolId,
            @RequestParam(value = "consumableId", required = false) Long consumableId,
            @RequestParam(value = "consumableQuantity", required = false) Integer consumableQuantity,
            @RequestParam(value = "partId", required = false) Long partId,
            @RequestPart(value = "newImage", required = false) MultipartFile file,
            Model model, Principal principal) {
        T oldItem = service.getById(id);
        item.setLinkedItems(oldItem.getLinkedItems());
        switch (action) {
            case "finish":
                return this.update(item, bindingResult, file, oldItem, model, principal);
            case "addDocument":
                this.itemsManipulation(item, 1, Document.class, documentId, 1);
                break;
            case "delDocument":
                this.itemsManipulation(item, 0, Document.class, documentId, 1);
                break;
            case "addFastener":
                this.itemsManipulation(item, 1, Fastener.class, fastenerId, fastenerQuantity);
                break;
            case "delFastener":
                this.itemsManipulation(item, 0, Fastener.class, fastenerId, fastenerQuantity);
                break;
            case "addTool":
                this.itemsManipulation(item, 1, Tool.class, toolId, 1);
                break;
            case "delTool":
                this.itemsManipulation(item, 0, Tool.class, toolId, 1);
                break;
            case "addConsumable":
                this.itemsManipulation(item, 1, Consumable.class, consumableId, consumableQuantity);
                break;
            case "delConsumable":
                this.itemsManipulation(item, 0, Consumable.class, consumableId, consumableQuantity);
                break;
            case "addPart":
                this.itemsManipulation(item, 1, Part.class, partId, 1);
                break;
            case "delPart":
                this.itemsManipulation(item, 0, Part.class, partId, 1);
                break;
        }
        service.update(oldItem, item);
        return editWithItem(model, oldItem, principal);
    }

    private void itemsManipulation(T item, int action, Class itemClass, Long id, int amount) {
        PartEntity entity = new PartEntity(thisClassNewObject.getClass().getSimpleName(),
                itemClass.getSimpleName(), id, amount);
        switch (action) {
            case 1 -> service.addToLinkedItems(item, entity);
            case 0 -> service.delFromLinkedItems(item, entity);
        }
    }

    @Override
    public ResponseEntity<Resource> createPdf(Long id, Principal principal) throws IOException {
        T item = this.service.getById(id);
        ServiceList generalList = serviceListController.getServiceList(item.getLinkedItems());

        return this.prepareServiceablePDF(item, principal, generalList);
    }

    @Operation(hidden = true)
    @GetMapping(value = "/pdfAll")
    public ResponseEntity<Resource> createPdfAll(@Param("id") Long id, Principal principal) throws IOException {
        T item = this.service.getById(id);
        ServiceList generalList = serviceListController.getGeneralServiceList(item.getLinkedItems());

        return this.prepareServiceablePDF(item, principal, generalList);
    }

    private void addLinkedItemsToModel(Model model, ServiceList serviceList) {
        model.addAttribute("documents", serviceList.getDocsMap());
        model.addAttribute("fasteners", serviceList.getFastenerMap());
        model.addAttribute("tools", serviceList.getToolMap());
        model.addAttribute("consumables", serviceList.getConsumableMap());
        model.addAttribute("parts", serviceList.getPartMap());
    }

    private void addAllItemsToModel(Model model) {
//        Future<List<Document>> allDocuments = mainExecutor.submit(() -> documentDAO.index());
//        Future<List<Fastener>> allFasteners = mainExecutor.submit(() -> fastenerDAO.index());
//        Future<List<Tool>> allTools = mainExecutor.submit(() -> toolDAO.index());
//        Future<List<Consumable>> allConsumables = mainExecutor.submit(() -> consumableDAO.index());
//        Future<List<Part>> allParts = mainExecutor.submit(() -> partDAO.index());
//        try {
//            model.addAttribute("allDocuments", allDocuments.get());
//            model.addAttribute("allFasteners", allFasteners.get());
//            model.addAttribute("allTools", allTools.get());
//            model.addAttribute("allConsumables", allConsumables.get());
//            model.addAttribute("allParts", allParts.get());
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }

        model.addAttribute("allDocuments", documentService.index());
        model.addAttribute("allFasteners", fastenerService.index());
        model.addAttribute("allTools", toolService.index());
        model.addAttribute("allConsumables", consumableService.index());
        model.addAttribute("allParts", partService.index());
    }

    @Override
    protected void addItemAttributesNew(Model model, T item, Principal principal) {
        super.addItemAttributesNew(model, item, principal);
    }

    @Autowired
    public void setServiceListController(ServiceListController serviceListController) {
        this.serviceListController = serviceListController;
    }
}
