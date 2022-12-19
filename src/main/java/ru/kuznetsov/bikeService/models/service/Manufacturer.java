package ru.kuznetsov.bikeService.models.service;

import javax.validation.constraints.NotEmpty;

public class Manufacturer {
    private int id;
    @NotEmpty(message = "Fill this field!")
    private final String name;
    private final String country;

    public Manufacturer(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Manufacturer(int id, String name){
        this(id, name, "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}
