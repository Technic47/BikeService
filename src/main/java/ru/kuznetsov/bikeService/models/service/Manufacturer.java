package ru.kuznetsov.bikeService.models.service;

import ru.kuznetsov.bikeService.models.Showable;

import javax.validation.constraints.NotEmpty;

public class Manufacturer implements Showable {
    private int id;
    @NotEmpty(message = "Fill this field!")
    private String name;
    private String country;

    public Manufacturer() {
        this.name = "";
        this.country = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String getValue() {
        return this.country;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
