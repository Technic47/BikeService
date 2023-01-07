package ru.kuznetsov.bikeService.models.bike;

import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PartsWithPartList extends SmallPart implements ServiceableWithParts {
    protected String partList;

    public PartsWithPartList() {
        Map<String, List<Integer>> newPartList = new HashMap<>();
        this.partList = this.converter.toJson(newPartList);
    }

    public String getPartList() {
        return this.partList;
    }

    public void setPartList(String partList) {
        this.partList = partList;
    }

    public Map<String, List<Integer>> returnPartListObject() {
        Type type = new TypeToken<Map<String, List<Integer>>>() {
        }.getType();
        return converter.fromJson(this.partList, type);
    }

    private void updatePartListObject(Map<String, List<Integer>> newPartList) {
        this.serviceList = converter.toJson(newPartList);
    }

    public void addToPartList(Serviceable item) {
        Map<String, List<Integer>> currentPartList = this.returnPartListObject();
        String targetClass = item.getClass().getSimpleName();
        List<Integer> targetList = currentPartList.get(targetClass);
        targetList.add(item.getId());
        currentPartList.put(targetClass, targetList);
        this.updatePartListObject(currentPartList);
    }

    public void delFromPartList(Serviceable item) {
        Map<String, List<Integer>> currentPartList = this.returnPartListObject();
        String targetClass = item.getClass().getSimpleName();
        List<Integer> targetList = currentPartList.get(targetClass);
        targetList.remove(item.getId());
        currentPartList.put(targetClass, targetList);
        this.updatePartListObject(currentPartList);
    }
}
