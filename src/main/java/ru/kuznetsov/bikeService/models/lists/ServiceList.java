package ru.kuznetsov.bikeService.models.lists;

import ru.kuznetsov.bikeService.models.showable.Showable;

import java.util.ArrayList;
import java.util.List;

public class ServiceList {
    private List<Long> toolList;
    private List<Long> fastenerList;
    private List<Long> consumableList;
    private List<Long> docsList;

    public ServiceList() {
        this.toolList = new ArrayList<>();
        this.fastenerList = new ArrayList<>();
        this.consumableList = new ArrayList<>();
        this.docsList = new ArrayList<>();
    }

    public List<Long> getToolList() {
        return toolList;
    }

    public void setToolList(List<Long> toolList) {
        this.toolList = toolList;
    }

    public List<Long> getFastenerList() {
        return fastenerList;
    }

    public void setFastenerList(List<Long> fastenerList) {
        this.fastenerList = fastenerList;
    }

    public List<Long> getConsumableList() {
        return consumableList;
    }

    public void setConsumableList(List<Long> consumableList) {
        this.consumableList = consumableList;
    }

    public List<Long> getDocsList() {
        return docsList;
    }

    public void setDocsList(List<Long> docsList) {
        this.docsList = docsList;
    }

    public void addToList(Showable obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Tool" -> this.addTool(obj.getId());
            case "Fastener" -> this.addFastener(obj.getId());
            case "Consumable" -> this.addConsumable(obj.getId());
            case "Document" -> this.addDocument(obj.getId());
        }
    }

    public void delFromList(Showable obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Tool" -> this.delTool(obj.getId());
            case "Fastener" -> this.delFastener(obj.getId());
            case "Consumable" -> this.delConsumable(obj.getId());
            case "Document" -> this.delDocument(obj.getId());
        }
    }

    public void addIdToList(PartEntity obj) {
        switch (obj.getType()) {
            case "Tool" -> this.addTool(obj.getItem_id());
            case "Fastener" -> this.addFastener(obj.getItem_id());
            case "Consumable" -> this.addConsumable(obj.getItem_id());
            case "Document" -> this.addDocument(obj.getItem_id());
        }
    }

    public void delIdFromList(PartEntity obj) {
        switch (obj.getType()) {
            case "Tool" -> this.delTool(obj.getItem_id());
            case "Fastener" -> this.delFastener(obj.getItem_id());
            case "Consumable" -> this.delConsumable(obj.getItem_id());
            case "Document" -> this.delDocument(obj.getItem_id());
        }
    }

    public void addTool(Long id) {
        this.toolList.add(id);
    }

    public void delTool(Long id) {
        this.toolList.remove(id);
    }

    public void addFastener(Long id) {
        this.fastenerList.add(id);
    }

    public void delFastener(Long id) {
        this.fastenerList.remove(id);
    }

    public void addConsumable(Long id) {
        this.consumableList.add(id);
    }

    public void delConsumable(Long id) {
        this.consumableList.remove(id);
    }

    public void addDocument(Long id) {
        this.docsList.add(id);
    }

    public void delDocument(Long id) {
        this.docsList.remove(id);
    }
}
