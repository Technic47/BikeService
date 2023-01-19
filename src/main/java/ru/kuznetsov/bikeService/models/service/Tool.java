package ru.kuznetsov.bikeService.models.service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import ru.kuznetsov.bikeService.models.abstracts.AbstractEntity;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "tools")
public class Tool extends AbstractEntity implements Usable {
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
    @Column(name = "manufacturer")
    protected Long manufacturer;
    @Column(name = "model")
    protected String model;
    @NotEmpty(message = "Fill this field!")
    @Column(name = "size")
    private String size;


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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getManufacturer() {
        return this.manufacturer;
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
        return this.size;
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
