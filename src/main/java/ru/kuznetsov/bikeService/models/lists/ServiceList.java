package ru.kuznetsov.bikeService.models.lists;

import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;

import java.util.*;

public class ServiceList {
    private List<Tool> toolList;
    private List<Fastener> fastenerList;
    private List<Consumable> consumableList;
    private List<Document> docsList;
    private List<Part> partList;
    private Map<Tool, Integer> toolMap;
    private Map<Fastener, Integer> fastenerMap;
    private Map<Consumable, Integer> consumableMap;
    private Map<Document, Integer> docsMap;
    private Map<Part, Integer> partMap;

    public ServiceList() {
        this.toolList = new ArrayList<>();
        this.fastenerList = new ArrayList<>();
        this.consumableList = new ArrayList<>();
        this.docsList = new ArrayList<>();
        this.partList = new ArrayList<>();
        this.toolMap = new HashMap<>();
        this.fastenerMap = new HashMap<>();
        this.consumableMap = new HashMap<>();
        this.docsMap = new HashMap<>();
        this.partMap = new HashMap<>();
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

    public Map<Tool, Integer> getToolMap() {
        return toolMap;
    }

    public void setToolMap(Map<Tool, Integer> toolMap) {
        this.toolMap = toolMap;
    }

    public Map<Fastener, Integer> getFastenerMap() {
        return fastenerMap;
    }

    public void setFastenerMap(Map<Fastener, Integer> fastenerMap) {
        this.fastenerMap = fastenerMap;
    }

    public Map<Consumable, Integer> getConsumableMap() {
        return consumableMap;
    }

    public void setConsumableMap(Map<Consumable, Integer> consumableMap) {
        this.consumableMap = consumableMap;
    }

    public Map<Document, Integer> getDocsMap() {
        return docsMap;
    }

    public void setDocsMap(Map<Document, Integer> docsMap) {
        this.docsMap = docsMap;
    }

    public Map<Part, Integer> getPartMap() {
        return partMap;
    }

    public void setPartMap(Map<Part, Integer> partMap) {
        this.partMap = partMap;
    }

    @Deprecated
    public boolean contains(PartEntity entity) {
        switch (entity.getType()) {
            case "Tool" -> {
                for (Tool tool : toolList) {
                    if (Objects.equals(tool.getId(), entity.getItem_id())) {
                        return true;
                    }
                }
            }

            case "Fastener" -> {
                for (Document doc : docsList) {
                    if (Objects.equals(doc.getId(), entity.getItem_id())) {
                        return true;
                    }
                }
            }
            case "Consumable" -> {
                for (Fastener fastener : fastenerList) {
                    if (Objects.equals(fastener.getId(), entity.getItem_id())) {
                        return true;
                    }
                }
            }
            case "Document" -> {
                for (Consumable consumable : consumableList) {
                    if (Objects.equals(consumable.getId(), entity.getItem_id())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void addToList(Showable obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Tool" -> this.addTool((Tool) obj);
            case "Fastener" -> this.addFastener((Fastener) obj);
            case "Consumable" -> this.addConsumable((Consumable) obj);
            case "Document" -> this.addDocument((Document) obj);
        }
    }

    @Deprecated
    public void delFromList(Showable obj) {
        switch (obj.getClass().getSimpleName()) {
            case "Tool" -> this.delTool((Tool) obj);
            case "Fastener" -> this.delFastener((Fastener) obj);
            case "Consumable" -> this.delConsumable((Consumable) obj);
            case "Document" -> this.delDocument((Document) obj);
        }
    }

    @Deprecated
    public void addEntityToMap(Showable obj, Integer amount) {
        switch (obj.getClass().getSimpleName()) {
            case "Tool" -> this.toolMap.put((Tool) obj, amount);
            case "Fastener" -> this.fastenerMap.put((Fastener) obj, amount);
            case "Consumable" -> this.consumableMap.put((Consumable) obj, amount);
            case "Document" -> this.docsMap.put((Document) obj, amount);
            case "Part" -> this.partMap.put((Part) obj, amount);
        }
    }

    public void addToToolMap(Tool obj, Integer amount) {
        this.toolMap.put(obj, amount);
    }

    public void addToFastenerMap(Fastener obj, Integer amount) {
        this.fastenerMap.put(obj, amount);
    }

    public void addToConsumableMap(Consumable obj, Integer amount) {
        this.consumableMap.put(obj, amount);
    }

    public void addToDocumentMap(Document obj, Integer amount) {
        this.docsMap.put(obj, amount);
    }

    public void addToPartMap(Part obj, Integer amount) {
        this.partMap.put(obj, amount);
    }

    @Deprecated
    public void delIdFromList(PartEntity obj) {
//        switch (obj.getType()) {
//            case "Tool" -> this.delTool(obj.getItem_id());
//            case "Fastener" -> this.delFastener(obj.getItem_id());
//            case "Consumable" -> this.delConsumable(obj.getItem_id());
//            case "Document" -> this.delDocument(obj.getItem_id());
//        }
    }

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
