package ru.kuznetsov.bikeService.models.service;

import ru.kuznetsov.bikeService.models.Showable;

import javax.validation.constraints.NotEmpty;

public class Tool implements Usable, Showable {
    private int id;
    private Manufacturer manufacturer;
    private String model;
    @NotEmpty(message = "Fill this field!")
    private String name;
    @NotEmpty(message = "Fill this field!")
    private String size;
    private String description;

    public Tool(int id, Manufacturer manufacturer, String model, String name, String size, String description) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.name = name;
        this.size = size;
        this.description = description;
    }

    public Tool(int id, String name, String size){
        this(id, null, "", name, size, "");
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

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
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
