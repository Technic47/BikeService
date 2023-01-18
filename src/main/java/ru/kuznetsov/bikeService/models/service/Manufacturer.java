package ru.kuznetsov.bikeService.models.service;

import jakarta.persistence.Entity;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.Showable;

import javax.validation.constraints.NotEmpty;

@Entity
public class Manufacturer extends AbstractShowableEntity {

    @NotEmpty(message = "Fill this field!")
    private String name;
    private String country;
    private String description;
    private int picture;
    private String value;

    public Manufacturer() {
        this.name = "";
        this.country = "";
        this.picture = 1;
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
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
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
