package ru.kuznetsov.bikeService.models.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import ru.kuznetsov.bikeService.models.AbstractEntity;
import ru.kuznetsov.bikeService.models.Showable;

import javax.validation.constraints.NotEmpty;

@Entity
public class Document extends AbstractEntity implements Showable {

    @NotEmpty(message = "Fill this field!")
    private String name;
    private String description;
    private String link;
    private int picture;
    private String value;

    public Document() {

    }

//    public Document() {
//        this.name = "";
//        this.description = "";
//        this.link = "";
//        this.picture = 1;
//    }



    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return this.link;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
