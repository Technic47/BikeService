package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.service.Manufacturer;

import java.util.ArrayList;
import java.util.List;

public class Unit extends SmallPart{
    private List<Part> partList;

    public Unit(int id, Manufacturer manufacturer, String model) {
        super(id, manufacturer, model);
        this.partList = new ArrayList<>();
    }

    public List<Part> getPartList() {
        return partList;
    }

    public void setPartList(List<Part> partList) {
        this.partList = partList;
    }
}
