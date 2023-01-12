package ru.kuznetsov.bikeService.models.documents;

import ru.kuznetsov.bikeService.models.Showable;

import javax.validation.constraints.NotEmpty;

public class Document implements Showable {

    private int id;
    @NotEmpty(message = "Fill this field!")
    private String name;
    private String description;
    private String link;
//    private int picture;

    public Document() {
        this.name = "";
        this.description = "";
        this.link = "";
//        this.picture = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return this.link;
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

//    public int getPicture() {
//        return picture;
//    }

//    public void setPicture(int picture) {
//        this.picture = picture;
//    }
}
