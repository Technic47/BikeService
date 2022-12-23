package ru.kuznetsov.bikeService.models.bike;

import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.JSONConverter;

import java.util.HashMap;
import java.util.Map;

@Component
public class PartsWithPartList extends SmallPart {
    protected final String partList;

    protected final JSONConverter<Map<String, Integer>> converterPartList;

    public PartsWithPartList() {
        this.converterPartList = new JSONConverter<>();
        Map<String, Integer> newPartList = new HashMap<>();
        this.partList = converterPartList.toJson(newPartList);
    }

    public Map<String, Integer> getPartList() {
        return converterPartList.fromJson(this.partList);
    }

    private void setPartList(Map<String, Integer> newPartList) {
        this.serviceList = converterPartList.toJson(newPartList);
    }

    public void addToPartList(Serviceable item) {
        Map<String, Integer> currentPartList = this.getPartList();
        currentPartList.put(item.getClass().getSimpleName(), item.getId());
        this.setPartList(currentPartList);
    }
}
