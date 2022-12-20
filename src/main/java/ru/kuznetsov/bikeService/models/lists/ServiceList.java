package ru.kuznetsov.bikeService.models.lists;

import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.models.service.Usable;

import java.util.List;

public class ServiceList {
    private List<Usable> toolList;
    private List<ru.kuznetsov.bikeService.models.service.Fastener> fastenerList;
    private List<Consumable> consumableList;
    private List<Document> docsList;

    public ServiceList(List<Usable> toolList, List<ru.kuznetsov.bikeService.models.service.Fastener> fastenerList, List<Consumable> consumableList, List<Document> docsList) {
        this.toolList = toolList;
        this.fastenerList = fastenerList;
        this.consumableList = consumableList;
        this.docsList = docsList;
    }

    public List<Usable> getToolList() {
        return toolList;
    }

    public void setToolList(List<Usable> toolList) {
        this.toolList = toolList;
    }

    public List<ru.kuznetsov.bikeService.models.service.Fastener> getFastenerList() {
        return fastenerList;
    }

    public void setFastenerList(List<ru.kuznetsov.bikeService.models.service.Fastener> fastenerList) {
        this.fastenerList = fastenerList;
    }

    public List<Consumable> getConsumableList() {
        return consumableList;
    }

    public void setConsumableList(List<Consumable> consumableList) {
        this.consumableList = consumableList;
    }

    public List<Document> getDocsList() {
        return docsList;
    }

    public void setDocsList(List<Document> docsList) {
        this.docsList = docsList;
    }
}
