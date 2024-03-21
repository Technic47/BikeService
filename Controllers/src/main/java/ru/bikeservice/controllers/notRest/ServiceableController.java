package ru.bikeservice.controllers.notRest;

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
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.lists.ServiceList;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class ServiceableController<T extends AbstractServiceableEntity>
        extends UsableController<T> {
    private ServiceListController serviceListController;
    private final Map<Integer, Set<PartEntity>> cacheSet;
    private final Map<Integer, ServiceList> cacheServiceList;
    private final Map<String, Set<AbstractShowableEntity>> globalMap;

    public ServiceableController() {
        cacheSet = new HashMap<>();
        cacheServiceList = new HashMap<>();
        globalMap = new HashMap<>();
    }

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Model model,
                       Principal principal) {
        T item = doShowProcedure(thisClassNewObject.getClass().getSimpleName(), id);
        if (item == null) {
            return "redirect:/" + category;
        }
        ServiceList serviceList = serviceListController.getServiceList(item.getLinkedItems());
        Long manufactureIndex = item.getManufacturer();
        model.addAttribute("manufacture", doShowProcedure(Manufacturer.class.getSimpleName(), manufactureIndex, principal));
        this.addLinkedItemsToModel(model, serviceList);
        this.addItemAttributesShow(model, item, principal);
        return super.show(item, model, principal, category);
    }

    @Override
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable Long id, Principal principal) {
        T item = doShowProcedure(thisClassNewObject.getClass().getSimpleName(), id);
        if (item == null) {
            return "redirect:/" + category;
        }
        cacheSet.put(getHashCodeForMap(item), item.getLinkedItems());
        fillGlobalMap();
        fillCacheServiceList(item);
        return this.editWithItem(model, item, principal);
    }

    private String editWithItem(Model model, T item, Principal principal) {
        ServiceList serviceList = cacheServiceList.get(getHashCodeForMap(item));
        this.addAllItemsToModel(model);
        this.addLinkedItemsToModel(model, serviceList);
        return super.edit(model, item, principal);
    }

    private int getHashCodeForMap(T item) {
        String code = item.getId() + item.getClass().getSimpleName();
        return code.hashCode();
    }

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

        switch (action) {
            case "finish" -> {
                int code = getHashCodeForMap(item);
                Set<PartEntity> partEntities = cacheSet.get(code);
                item.setLinkedItems(partEntities);
                T oldItem = doShowProcedure(thisClassNewObject.getClass().getSimpleName(), id);
                cacheSet.remove(code);
                cacheServiceList.remove(code);
                globalMap.clear();
                return this.update(item, bindingResult, file, oldItem, model, principal);
            }
            case "addDocument" -> this.itemsManipulation(item, 1, Document.class, documentId, 1);
            case "delDocument" -> this.itemsManipulation(item, 0, Document.class, documentId, 1);
            case "addFastener" -> this.itemsManipulation(item, 1, Fastener.class, fastenerId, fastenerQuantity);
            case "delFastener" -> this.itemsManipulation(item, 0, Fastener.class, fastenerId, fastenerQuantity);
            case "addTool" -> this.itemsManipulation(item, 1, Tool.class, toolId, 1);
            case "delTool" -> this.itemsManipulation(item, 0, Tool.class, toolId, 1);
            case "addConsumable" -> this.itemsManipulation(item, 1, Consumable.class, consumableId, consumableQuantity);
            case "delConsumable" -> this.itemsManipulation(item, 0, Consumable.class, consumableId, consumableQuantity);
            case "addPart" -> this.itemsManipulation(item, 1, Part.class, partId, 1);
            case "delPart" -> this.itemsManipulation(item, 0, Part.class, partId, 1);
        }
        return editWithItem(model, item, principal);
    }

    private void itemsManipulation(T item, int action, Class itemClass, Long id, int amount) {
        String type = thisClassNewObject.getClass().getSimpleName();
        PartEntity entity = new PartEntity(type,
                itemClass.getSimpleName(), id, amount);

        switch (action) {
            case 1 -> addToSet(item, entity);
            case 0 -> delFromSet(item, entity);
        }
    }

    private void addToSet(T item, PartEntity entity) {
        int hashCode = getHashCodeForMap(item);
        Set<PartEntity> set = cacheSet.get(hashCode);

        ServiceList serviceList = cacheServiceList.get(hashCode);

        serviceListController.addToServiceList(serviceList, getObjectFromGMap(entity), entity.getAmount());

        if (set.contains(entity)) {
            if (entity.getType().equals("Consumable") || entity.getType().equals("Fastener")) {
                set.forEach(setItem -> {
                    if (setItem.equals(entity)) {
                        setItem.setAmount(setItem.getAmount() + entity.getAmount());
                    }
                });
            }
        } else {
            entity.setAmount(1);
            set.add(entity);
        }
    }

    private void delFromSet(T item, PartEntity entity) {
        int hashCode = getHashCodeForMap(item);
        Set<PartEntity> set = cacheSet.get(hashCode);

        ServiceList serviceList = cacheServiceList.get(hashCode);

        serviceListController.delFromServiceList(serviceList, getObjectFromGMap(entity), entity.getAmount());

        if (set.contains(entity)) {
            if (entity.getType().equals("Consumable") || entity.getType().equals("Fastener")) {
                set.forEach(setItem -> {
                    if (setItem.equals(entity)) {
                        setItem.setAmount(setItem.getAmount() - entity.getAmount());
                    }
                });
            } else set.remove(entity);
        }

        Set<PartEntity> collect = set
                .stream()
                .filter(e -> e.getAmount() > 0)
                .collect(Collectors.toSet());

        item.setLinkedItems(collect);
    }

    private AbstractShowableEntity getObjectFromGMap(PartEntity entity) {
        String type = entity.getType();
        Long id = entity.getItemId();

        AbstractShowableEntity result = null;

        switch (type) {
            case "Document" -> result = findEntity(globalMap.get("allDocuments"), id);
            case "Fastener" -> result = findEntity(globalMap.get("allFasteners"), id);
            case "Tool" -> result = findEntity(globalMap.get("allTools"), id);
            case "Consumable" -> result = findEntity(globalMap.get("allConsumables"), id);
            case "Part" -> result = findEntity(globalMap.get("allParts"), id);
        }
        return result;
    }

    private AbstractShowableEntity findEntity(Set<AbstractShowableEntity> set, Long id) {
        Optional<AbstractShowableEntity> result = set.stream().filter(item -> item.getId().equals(id)).findFirst();
        return result.orElse(null);
    }

    @Override
    public ResponseEntity<Resource> createPdf(Long id, Principal principal) {
        T item = doShowProcedure(thisClassNewObject.getClass().getSimpleName(), id);
        ServiceList generalList = serviceListController.getServiceList(item.getLinkedItems());

        return this.prepareServiceablePDF(item, principal, generalList);
    }

    @GetMapping(value = "/pdfAll")
    public ResponseEntity<Resource> createPdfAll(@Param("id") Long id, Principal principal) {
        T item = doShowProcedure(thisClassNewObject.getClass().getSimpleName(), id);
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

        model.addAttribute("allDocuments", globalMap.get("allDocuments"));
        model.addAttribute("allFasteners", globalMap.get("allFasteners"));
        model.addAttribute("allTools", globalMap.get("allTools"));
        model.addAttribute("allConsumables", globalMap.get("allConsumables"));
        model.addAttribute("allParts", globalMap.get("allParts"));

//        model.addAttribute("allDocuments", doIndexProcedure(Document.class.getSimpleName()));
//        model.addAttribute("allFasteners", doIndexProcedure(Fastener.class.getSimpleName()));
//        model.addAttribute("allTools", doIndexProcedure(Tool.class.getSimpleName()));
//        model.addAttribute("allConsumables", doIndexProcedure(Consumable.class.getSimpleName()));
//        model.addAttribute("allParts", doIndexProcedure(Part.class.getSimpleName()));
    }

    private void fillGlobalMap() {
        globalMap.put("allDocuments", new HashSet<>(doIndexProcedure(Document.class.getSimpleName())));
        globalMap.put("allFasteners", new HashSet<>(doIndexProcedure(Fastener.class.getSimpleName())));
        globalMap.put("allTools", new HashSet<>(doIndexProcedure(Tool.class.getSimpleName())));
        globalMap.put("allConsumables", new HashSet<>(doIndexProcedure(Consumable.class.getSimpleName())));
        globalMap.put("allParts", new HashSet<>(doIndexProcedure(Part.class.getSimpleName())));
    }

    private void fillCacheServiceList(T item) {
        ServiceList serviceList = serviceListController.getServiceList(cacheSet.get(getHashCodeForMap(item)));
        cacheServiceList.put(getHashCodeForMap(item), serviceList);
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
