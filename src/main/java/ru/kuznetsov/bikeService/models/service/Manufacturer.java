package ru.kuznetsov.bikeService.models.service;

import javax.validation.constraints.NotEmpty;

public class Manufacturer {
    private int manufacturerid;
    @NotEmpty(message = "Fill this field!")
    private String name;
    private String country;

    public Manufacturer() {
        this.name = "";
        this.country = "";
    }

    public int getManufacturerid() {
        return manufacturerid;
    }

    public void setManufacturerid(int manufacturerid) {
        this.manufacturerid = manufacturerid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }



    @Override
    public String toString() {
        return "Manufacturer{" +
                "manufacturerid=" + manufacturerid +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
