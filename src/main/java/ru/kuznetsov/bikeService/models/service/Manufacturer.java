package ru.kuznetsov.bikeService.models.service;

import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.abstracts.BaseEntity;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "manufacturers")
public class Manufacturer extends BaseEntity implements Showable {

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
    protected Long picture;
    @Transient
    protected String value;
    @Column(name = "country")
    private String country;

//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public Long getPicture() {
        return picture;
    }

    public void setPicture(Long picture) {
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
