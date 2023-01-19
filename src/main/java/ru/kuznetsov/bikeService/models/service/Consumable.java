package ru.kuznetsov.bikeService.models.service;

import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.abstracts.BaseEntity;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "consumables")
public class Consumable extends BaseEntity implements Usable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    protected Long id;
    @NotEmpty(message = "Fill this field!")
    @Column(name = "name")
    protected String name;
    @Column(name = "description")
    protected String description;
    //    @Column(name="link")
//    protected String link;
    @Column(name = "picture")
    protected Long picture;
    @Transient
    protected String value;
    @NotEmpty(message = "Fill this field!")
    @Column(name = "volume")
    private String volume;

    @Column(name = "manufacturer")
    protected Long manufacturer;
    @Column(name = "model")
    protected String model;

//    public Consumable() {
//        this.manufacturer = 0;
//        this.model = "";
//        this.name = "";
//        this.volume = "";
//        this.description = "";
//    }
//


//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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

    public Long getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Long manufacturer) {
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
    public Long getPicture() {
        return picture;
    }

    public void setPicture(Long picture) {
        this.picture = picture;
    }
}
