package ru.kuznetsov.bikeService.models.lists;

import ru.kuznetsov.bikeService.models.Showable;

import java.util.ArrayList;
import java.util.List;

public class ServiceList {
    private int id;
    private List<Integer> toolList;
    private List<Integer> fastenerList;
    private List<Integer> consumableList;
    private List<Integer> docsList;

    public ServiceList() {
        this.toolList = new ArrayList<>();
        this.fastenerList = new ArrayList<>();
        this.consumableList = new ArrayList<>();
        this.docsList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getToolList() {
        return toolList;
    }

    public void setToolList(List<Integer> toolList) {
        this.toolList = toolList;
    }

    public List<Integer> getFastenerList() {
        return fastenerList;
    }

    public void setFastenerList(List<Integer> fastenerList) {
        this.fastenerList = fastenerList;
    }

    public List<Integer> getConsumableList() {
        return consumableList;
    }

    public void setConsumableList(List<Integer> consumableList) {
        this.consumableList = consumableList;
    }

    public List<Integer> getDocsList() {
        return docsList;
    }

    public void setDocsList(List<Integer> docsList) {
        this.docsList = docsList;
    }

    public void addToList(Showable obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Tool":
                this.addTool(obj.getId());
                break;
            case "Fastener":
                this.addFastener(obj.getId());
                break;
            case "Consumable":
                this.addConsumable(obj.getId());
                break;
            case "Document":
                this.addDocument(obj.getId());
                break;
        }
    }

    public void delFromList(Showable obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Tool":
                this.delTool(obj.getId());
                break;
            case "Fastener":
                this.delFastener(obj.getId());
                break;
            case "Consumable":
                this.delConsumable(obj.getId());
                break;
            case "Document":
                this.delDocument(obj.getId());
                break;
        }
    }

    public void addTool(Integer id) {
        this.toolList.add(id);
    }

    public void delTool(Integer id) {
        this.toolList.remove(id);
    }

    public void addFastener(Integer id) {
        this.fastenerList.add(id);
    }

    public void delFastener(Integer id) {
        this.fastenerList.remove(id);
    }

    public void addConsumable(Integer id) {
        this.consumableList.add(id);
    }

    public void delConsumable(Integer id) {
        this.consumableList.remove(id);
    }

    public void addDocument(Integer id) {
        this.docsList.add(id);
    }

    public void delDocument(Integer id) {
        this.docsList.remove(id);
    }
}
