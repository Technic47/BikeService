package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;

import java.util.Set;

@Component
public class ServiceListController extends AbstractController {
    public ServiceList getServiceList(Set<PartEntity> entityList) {
        ServiceList serviceList = new ServiceList();
        for (PartEntity entity : entityList) {
            switch (entity.getType()) {
                case "Tool" ->
                        serviceList.addToToolMap(this.toolService.getById(entity.getItemId()), entity.getAmount());
                case "Fastener" ->
                        serviceList.addToFastenerMap(this.fastenerService.getById(entity.getItemId()), entity.getAmount());
                case "Consumable" ->
                        serviceList.addToConsumableMap(this.consumableService.getById(entity.getItemId()), entity.getAmount());
                case "Document" ->
                        serviceList.addToDocumentMap(this.documentService.getById(entity.getItemId()), entity.getAmount());
                case "Part" ->
                        serviceList.addToPartMap(this.partService.getById(entity.getItemId()), entity.getAmount());
            }
        }
        return serviceList;
    }

    public ServiceList getGeneralServiceList(Set<PartEntity> entityList) {
        ServiceList generalList = new ServiceList();
        ServiceList itemServiceList = this.getServiceList(entityList);
        generalList.addAllToList(itemServiceList);

        itemServiceList.getPartMap().keySet().forEach(part -> {
            ServiceList partServiceList = this.getServiceList(part.getLinkedItems());
            generalList.addAllToList(partServiceList);
        });
        return generalList;
    }
}
