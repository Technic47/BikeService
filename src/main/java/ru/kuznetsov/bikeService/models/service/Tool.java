package ru.kuznetsov.bikeService.models.service;

import ru.kuznetsov.bikeService.models.Showable;

import javax.validation.constraints.NotEmpty;

public class Tool implements Usable, Showable {
    private int id;
    private int manufacturer;
    private String model;
    @NotEmpty(message = "Fill this field!")
    private String name;
    @NotEmpty(message = "Fill this field!")
    private String size;
    private String description;

    public Tool() {
        this.manufacturer = 0;
        this.model = "";
        this.name = "";
        this.size = "";
        this.description = "";
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(int manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.size;
    }
}
