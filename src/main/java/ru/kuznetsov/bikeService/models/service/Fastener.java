package ru.kuznetsov.bikeService.models.service;

import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.abstracts.BaseEntity;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "fasteners")
public class Fastener extends BaseEntity implements Showable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @NotEmpty(message = "Fill this field!")
    @Column(name = "name")
    protected String name;
    @Column(name = "description")
    protected String description;
    //    @Column(name="link")
//    protected String link;
    @Column(name = "picture")
    protected int picture;
    @Transient
    protected String value;
    @NotEmpty(message = "Fill this field!")
    @Column(name = "specs")
    private String specs;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
