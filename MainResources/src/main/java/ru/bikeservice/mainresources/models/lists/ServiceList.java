package ru.bikeservice.mainresources.models.lists;

import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;

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

    /**
     * Add all Maps from newList to current.
     * Summarize consumableMap`s and fastenerMap`s values. Other are rewritten if doubles.
     * @param newList other ServiceList
     */
    public void addAllToList(ServiceList newList){
        newList.getFastenerMap().forEach((key, value) ->{
            if (this.fastenerMap.containsKey(key)) {
                Integer amount = this.fastenerMap.getOrDefault(key, 1);
                this.fastenerMap.replace(key, amount + value);
            } else{
                this.fastenerMap.put(key, value);
            }
        });
        newList.getConsumableMap().forEach((key, value) ->{
            if (this.consumableMap.containsKey(key)) {
                Integer amount = this.consumableMap.getOrDefault(key, 1);
                this.consumableMap.replace(key, amount + value);
            } else{
                this.consumableMap.put(key, value);
            }
        });
        newList.getDocsMap().forEach(this::addToDocumentMap);
        newList.getToolMap().forEach(this::addToToolMap);
        newList.getPartMap().forEach(this::addToPartMap);
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
