package ru.kuznetsov.bikeService.models.lists;

import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;

import java.util.HashMap;
import java.util.Map;

public class ServiceList {
    private final Map<Tool, Integer> toolMap;
    private final Map<Fastener, Integer> fastenerMap;
    private final Map<Consumable, Integer> consumableMap;
    private final Map<Document, Integer> docsMap;
    private final Map<Part, Integer> partMap;

    public ServiceList() {
        this.toolMap = new HashMap<>();
        this.fastenerMap = new HashMap<>();
        this.consumableMap = new HashMap<>();
        this.docsMap = new HashMap<>();
        this.partMap = new HashMap<>();
    }

    public Map<Tool, Integer> getToolMap() {
        return toolMap;
    }

    public Map<Fastener, Integer> getFastenerMap() {
        return fastenerMap;
    }

    public Map<Consumable, Integer> getConsumableMap() {
        return consumableMap;
    }

    public Map<Document, Integer> getDocsMap() {
        return docsMap;
    }

    public Map<Part, Integer> getPartMap() {
        return partMap;
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
}
