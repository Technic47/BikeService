package ru.kuznetsov.bikeService.models.service;

import javax.validation.constraints.NotEmpty;

public class Manufacturer {
    private int manufacturerid;
    @NotEmpty(message = "Fill this field!")
    private final String name;
    private final String country;

    public Manufacturer(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public Manufacturer(String name){
        this(name, "");
    }

    public Manufacturer(){
        this("");
    }

    public int getManufacturerid() {
        return manufacturerid;
    }

    public void setManufacturerid(int manufacturerid) {
        this.manufacturerid = manufacturerid;
    }

    public String setName(String name) {
        return this.name;
    }

    public String setCountry(String country) {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}
