package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.service.Manufacturer;

import java.util.ArrayList;
import java.util.List;

public class Bike extends PartsWithPartList<Unit>{
    private String year;

    public Bike() {
        this.year = "";
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
