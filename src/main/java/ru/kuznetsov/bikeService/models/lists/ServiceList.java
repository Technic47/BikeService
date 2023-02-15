package ru.kuznetsov.bikeService.models.lists;

import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;

import java.util.ArrayList;
import java.util.List;

public class ServiceList {
    private List<Tool> toolList;
    private List<Fastener> fastenerList;
    private List<Consumable> consumableList;
    private List<Document> docsList;

    private List<Part> partList;

    public ServiceList() {
        this.toolList = new ArrayList<>();
        this.fastenerList = new ArrayList<>();
        this.consumableList = new ArrayList<>();
        this.docsList = new ArrayList<>();
        this.partList = new ArrayList<>();
    }

    public List<Tool> getToolList() {
        return toolList;
    }

    public void setToolList(List<Tool> toolList) {
        this.toolList = toolList;
    }

    public List<Fastener> getFastenerList() {
        return fastenerList;
    }

    public void setFastenerList(List<Fastener> fastenerList) {
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

    public List<Part> getPartList() {
        return partList;
    }

    public void setPartList(List<Part> partList) {
        this.partList = partList;
    }

    public void addToList(Showable obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Tool" -> this.addTool((Tool)obj);
            case "Fastener" -> this.addFastener((Fastener) obj);
            case "Consumable" -> this.addConsumable((Consumable) obj);
            case "Document" -> this.addDocument((Document) obj);
        }
    }

    public void delFromList(Showable obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Tool" -> this.delTool((Tool)obj);
            case "Fastener" -> this.delFastener((Fastener) obj);
            case "Consumable" -> this.delConsumable((Consumable) obj);
            case "Document" -> this.delDocument((Document) obj);
        }
    }

//    public void addIdToList(PartEntity obj) {
//        switch (obj.getType()) {
//            case "Tool" -> this.addTool(obj.getItem_id());
//            case "Fastener" -> this.addFastener(obj.getItem_id());
//            case "Consumable" -> this.addConsumable(obj.getItem_id());
//            case "Document" -> this.addDocument(obj.getItem_id());
//        }
//    }
//
//    public void delIdFromList(PartEntity obj) {
//        switch (obj.getType()) {
//            case "Tool" -> this.delTool(obj.getItem_id());
//            case "Fastener" -> this.delFastener(obj.getItem_id());
//            case "Consumable" -> this.delConsumable(obj.getItem_id());
//            case "Document" -> this.delDocument(obj.getItem_id());
//        }
//    }

    public void addTool(Tool id) {
        this.toolList.add(id);
    }

    public void delTool(Tool id) {
        this.toolList.remove(id);
    }

    public void addFastener(Fastener id) {
        this.fastenerList.add(id);
    }

    public void delFastener(Fastener id) {
        this.fastenerList.remove(id);
    }

    public void addConsumable(Consumable id) {
        this.consumableList.add(id);
    }

    public void delConsumable(Consumable id) {
        this.consumableList.remove(id);
    }

    public void addDocument(Document id) {
        this.docsList.add(id);
    }

    public void delDocument(Document id) {
        this.docsList.remove(id);
    }

    public void addPart(Part id) {
        this.partList.add(id);
    }

    public void delPart(Part id) {
        this.partList.remove(id);
    }
}
