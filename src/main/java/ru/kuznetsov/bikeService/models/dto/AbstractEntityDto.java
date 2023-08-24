package ru.kuznetsov.bikeService.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Usable;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AbstractEntityDto {
    private Long id;
    private String name;
    private String description;
    private Long picture;
    private String link;
    private String value;
    private Long manufacturer;
    private String model;
    private Set<PartEntity> linkedItems;

    public AbstractEntityDto(Showable toConvert) {
        if (toConvert instanceof Usable) {
            this.manufacturer = ((Usable) toConvert).getManufacturer();
            this.model = ((Usable) toConvert).getModel();
        }
        if (toConvert instanceof Serviceable) {
            this.linkedItems = ((Serviceable) toConvert).getLinkedItems();
        }
        this.id = toConvert.getId();
        this.name = toConvert.getName();
        this.description = toConvert.getDescription();
        this.picture = toConvert.getPicture();
        this.link = toConvert.getLink();
        this.value = toConvert.getValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<PartEntity> getLinkedItems() {
        return linkedItems;
    }

    public void setLinkedItems(Set<PartEntity> linkedItems) {
        this.linkedItems = linkedItems;
    }
}
