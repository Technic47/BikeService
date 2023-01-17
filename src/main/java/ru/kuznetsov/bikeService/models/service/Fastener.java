package ru.kuznetsov.bikeService.models.service;

import jakarta.persistence.Entity;
import ru.kuznetsov.bikeService.models.AbstractEntity;
import ru.kuznetsov.bikeService.models.Showable;

import javax.validation.constraints.NotEmpty;
@Entity
public class Fastener extends AbstractEntity implements Showable {

    @NotEmpty(message = "Fill this field!")
    private String name;
    @NotEmpty(message = "Fill this field!")
    private String specs;
    private String description;
    private int picture;
    private String value;

    public Fastener(String name, String specs, String description) {
        this.name = name;
        this.specs = specs;
        this.description = description;
    }

    public Fastener(String name, String specs) {
        this(name, specs, "");
    }

    public Fastener() {
        this("", "");
    }



    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.specs;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public String getSpecs() {
        return specs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
