package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.abstracts.CommonServiceableEntityService;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public abstract class ServiceableControllerREST<T extends AbstractServiceableEntity,
        S extends CommonServiceableEntityService<T>>
        extends UsableControllerREST<T, S> {


    protected ServiceableControllerREST(S service, PDFService pdfService, ManufacturerService manufacturerService) {
        super(service, pdfService, manufacturerService);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity show(@PathVariable("id") Long id,
                               Principal principal) {
        Map<Object, Object> response = new HashMap<>();
        T item = service.getById(id);
        super.show(item, response, principal);
        return ResponseEntity.ok(response);
    }

    @Override
    protected void addItemAttributesShow(Map<Object, Object> response, T item, Principal principal) {
        response.put("linkedItems", item.getLinkedItems());
        super.addItemAttributesShow(response, item, principal);
    }

    //    private ServiceList getServiceList(Set<PartEntity> entityList) {
//        ServiceList serviceList = new ServiceList();
//        for (PartEntity entity : entityList) {
//            switch (entity.getType()) {
//                case "Tool" -> serviceList.addToToolMap(this.toolDAO.getById(entity.getItemId()), entity.getAmount());
//                case "Fastener" ->
//                        serviceList.addToFastenerMap(this.fastenerDAO.getById(entity.getItemId()), entity.getAmount());
//                case "Consumable" ->
//                        serviceList.addToConsumableMap(this.consumableDAO.getById(entity.getItemId()), entity.getAmount());
//                case "Document" ->
//                        serviceList.addToDocumentMap(this.documentDAO.getById(entity.getItemId()), entity.getAmount());
//                case "Part" -> serviceList.addToPartMap(this.partDAO.getById(entity.getItemId()), entity.getAmount());
//            }
//        }
//        return serviceList;
//    }

}
