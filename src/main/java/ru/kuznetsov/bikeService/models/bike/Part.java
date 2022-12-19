package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.service.Manufacturer;

import java.util.ArrayList;
import java.util.List;

public class Part extends SmallPart{
    private List<SmallPart> smallParts;

    public Part(int id, Manufacturer manufacturer, String model) {
        super(id, manufacturer, model);
        this.smallParts = new ArrayList<>();
    }

    public List<SmallPart> getSmallParts() {
        return smallParts;
    }

    public void setSmallParts(List<SmallPart> smallParts) {
        this.smallParts = smallParts;
    }
}
