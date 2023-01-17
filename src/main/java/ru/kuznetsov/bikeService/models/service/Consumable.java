package ru.kuznetsov.bikeService.models.service;

import ru.kuznetsov.bikeService.models.AbstractEntity;

import javax.validation.constraints.NotEmpty;

public class Consumable extends AbstractEntity implements Usable {

    private int manufacturer;
    private String model;
    @NotEmpty(message = "Fill this field!")
    private String name;
    @NotEmpty(message = "Fill this field!")
    private String volume;
    private String description;
    private int picture;
    private String value;

    public Consumable() {
        this.manufacturer = 0;
        this.model = "";
        this.name = "";
        this.volume = "";
        this.description = "";
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public int getManufacturer() {
        return manufacturer;
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
        return this.volume;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
