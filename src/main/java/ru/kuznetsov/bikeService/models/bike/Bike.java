package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.service.Manufacturer;

import java.util.ArrayList;
import java.util.List;

public class Bike extends SmallPart{
    private List<Unit> unitList;
    private String year;

    public Bike(int id, Manufacturer manufacturer, String model) {
        super(id, manufacturer, model);
        this.unitList = new ArrayList<>();
        this.year = "";
    }

    public List<Unit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<Unit> unitList) {
        this.unitList = unitList;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
