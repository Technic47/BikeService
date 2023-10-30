package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Component;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.lists.ServiceList;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractEntityController;

import java.util.Map;
import java.util.Set;

@Component
public class ServiceListController extends AbstractEntityController {
    public ServiceList getServiceList(Set<PartEntity> entityList) {
        ServiceList serviceList = new ServiceList();
        for (PartEntity entity : entityList) {
            switch (entity.getType()) {
                case "Tool" ->
                        serviceList.addToToolMap(doShowProcedure(Tool.class.getSimpleName(), entity.getItemId()), entity.getAmount());
                case "Fastener" ->
                        serviceList.addToFastenerMap(doShowProcedure(Fastener.class.getSimpleName(), entity.getItemId()), entity.getAmount());
                case "Consumable" ->
                        serviceList.addToConsumableMap(doShowProcedure(Consumable.class.getSimpleName(), entity.getItemId()), entity.getAmount());
                case "Document" ->
                        serviceList.addToDocumentMap(doShowProcedure(Document.class.getSimpleName(), entity.getItemId()), entity.getAmount());
                case "Part" ->
                        serviceList.addToPartMap(doShowProcedure(Part.class.getSimpleName(), entity.getItemId()), entity.getAmount());
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

    public void addToServiceList(ServiceList serviceList, AbstractShowableEntity entity, Integer amount) {
        switch (entity.getClass().getSimpleName()) {
            case "Document" -> addWithExistCheck(serviceList.getDocsMap(), entity, amount);
            case "Fastener" -> addWithExistCheck(serviceList.getFastenerMap(), entity, amount);
            case "Tool" -> addWithExistCheck(serviceList.getToolMap(), entity, amount);
            case "Consumable" -> addWithExistCheck(serviceList.getConsumableMap(), entity, amount);
            case "Part" -> addWithExistCheck(serviceList.getPartMap(), entity, amount);
        }
    }

    private <T extends AbstractShowableEntity> void addWithExistCheck(Map<T, Integer> map, AbstractShowableEntity entity, Integer amount) {
        T key = (T) entity;
        if (map.containsKey(key)) {
            Integer newAmount = map.get(key) + amount;
            map.put(key, newAmount);
        } else map.put(key, amount);
    }

    public void delFromServiceList(ServiceList serviceList, AbstractShowableEntity entity, Integer amount) {
        switch (entity.getClass().getSimpleName()) {
            case "Document" -> delWithExistCheck(serviceList.getDocsMap(), entity, amount);
            case "Fastener" -> delWithExistCheck(serviceList.getFastenerMap(), entity, amount);
            case "Tool" -> delWithExistCheck(serviceList.getToolMap(), entity, amount);
            case "Consumable" -> delWithExistCheck(serviceList.getConsumableMap(), entity, amount);
            case "Part" -> delWithExistCheck(serviceList.getPartMap(), entity, amount);
        }
    }

    private <T extends AbstractShowableEntity> void delWithExistCheck(Map<T, Integer> map, AbstractShowableEntity entity, Integer amount) {
        T key = (T) entity;
        if (map.containsKey(key)) {
            int newAmount = map.get(key) - amount;
            if (newAmount <= 0) {
                map.remove(key);
            } else map.put(key, newAmount);
        }
    }
}
