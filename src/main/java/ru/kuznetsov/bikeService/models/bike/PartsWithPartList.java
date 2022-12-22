package ru.kuznetsov.bikeService.models.bike;

import java.util.HashMap;
import java.util.Map;

public class PartsWithPartList<T extends Serviceable> extends SmallPart {
    private final Map<String, Integer> partList;

    public PartsWithPartList() {
        this.partList = new HashMap<>();
    }

    public void addPartToList(T part) {
        this.partList.put(part.getClass().getSimpleName(), part.getId());
    }

    public Map<String, Integer> getPartList() {
        return this.partList;
    }
}
