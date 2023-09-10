package ru.kuznetsov.pdfmodule;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

/**
 * Dto to show entities to users. Enable hiding service information.
 */
@JsonInclude(JsonInclude.Include.NON_NULL) //Hide null fields
public class PdfEntityDto {
    private String category;
    private String name;
    private String description;
    private String picture;
    private String link;
    private String valueName;
    private String value;
    private String manufacturer;
    private String model;
    private Set<PdfEntityDto> linkedItems;

    public PdfEntityDto() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Set<PdfEntityDto> getLinkedItems() {
        return linkedItems;
    }

    public void setLinkedItems(Set<PdfEntityDto> linkedItems) {
        this.linkedItems = linkedItems;
    }

    public String getCredentials(){
        return this.valueName + " - " + this.value;
    }
}
