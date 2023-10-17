package ru.bikeservice.mainresources.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.models.usable.Usable;

/**
 * Dto to get new entities from users. Enable hiding service information.
 */
@JsonInclude(JsonInclude.Include.NON_NULL) //Hide null fields
public class AbstractEntityDtoNew {
    private String category;
    private String name;
    private String description;
    private Long picture;
    private String link;
    private String value;
    private Long creator;
    private boolean shared;
    private Long manufacturer;
    private String model;

    public AbstractEntityDtoNew() {
    }

    public AbstractEntityDtoNew(Showable item, String category) {
        this.name = item.getName();
        this.description = item.getDescription();
        this.picture = item.getPicture();
        this.link = item.getLink();
        this.value = item.getValue();
        this.creator = item.getCreator();
        this.category = category;
        if (item instanceof Usable) {
            this.manufacturer = ((Usable) item).getManufacturer();
            this.model = ((Usable) item).getModel();
        }
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

    public Long getPicture() {
        return picture;
    }

    public void setPicture(Long picture) {
        this.picture = picture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
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

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
