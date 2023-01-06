package ru.kuznetsov.bikeService.models.bike;

import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class PartsWithPartList extends SmallPart {
    protected final String partList;

    public PartsWithPartList() {
        Map<String, Integer> newPartList = new HashMap<>();
        this.partList = converter.toJson(newPartList);
    }

    public String getPartList() {
        return this.partList;
    }

    private Map<String, Integer> returnPartListObject() {
        Type type = new TypeToken<Map<String, Integer>>() {
        }.getType();
        return converter.fromJson(this.partList, type);
    }

    private void setPartList(Map<String, Integer> newPartList) {
        this.serviceList = converter.toJson(newPartList);
    }

    public void addToPartList(Serviceable item) {
        Map<String, Integer> currentPartList = this.returnPartListObject();
        currentPartList.put(item.getClass().getSimpleName(), item.getId());
        this.setPartList(currentPartList);
    }

    public void delFromPartList(Serviceable item) {
        Map<String, Integer> currentPartList = this.returnPartListObject();
        currentPartList.remove(item.getClass().getSimpleName(), item.getId());
        this.setPartList(currentPartList);
    }
}
