package ru.kuznetsov.bikeService.models.documents;

import javax.validation.constraints.NotEmpty;

public class Document {

    private int docid;
    @NotEmpty
    private String name;
    private String description;
    private String link;

    public Document(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public Document(){
        this.name="";
        this.description = "";
        this.link = "";
    }

    public int getId() {
        return docid;
    }

    public void setId(int id) {
        this.docid = id;
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return "Document{" +
                "id=" + docid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
