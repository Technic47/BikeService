package ru.kuznetsov.bikeService.models.service;

import jakarta.persistence.Entity;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;

import javax.validation.constraints.NotEmpty;

@Entity
public class Tool extends AbstractUsableEntity {

    private int manufacturer;
    private String model;
    @NotEmpty(message = "Fill this field!")
    private String name;
    @NotEmpty(message = "Fill this field!")
    private String size;
    private String description;
    private int picture;
    private String value;

    public Tool() {
        this.manufacturer = 0;
        this.model = "";
        this.name = "";
        this.size = "";
        this.description = "";
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
