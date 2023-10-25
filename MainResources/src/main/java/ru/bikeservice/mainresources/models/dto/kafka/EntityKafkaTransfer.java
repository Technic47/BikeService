package ru.bikeservice.mainresources.models.dto.kafka;

import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.servicable.Bike;
import ru.bikeservice.mainresources.models.servicable.Part;
import ru.bikeservice.mainresources.models.showable.Document;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.models.usable.Usable;

import java.util.Set;

/**
 * Dto to transfer entities via kafka. Enable hiding service information.
 */
public class EntityKafkaTransfer {
    private Long id;
    private String type;
    private String name;
    private String description;
    private Long picture;
    private String link;
    private String value;
    private Long creator;
    private boolean shared;
    private Long manufacturer;
    private String model;
    private Set<PartEntity> linkedItems;

    public EntityKafkaTransfer() {
    }

    public EntityKafkaTransfer(Showable item, String type) {
        this.id = item.getId();
        this.type = type;
        this.name = item.getName();
        this.description = item.getDescription();
        this.picture = item.getPicture();
        this.link = item.getLink();
        this.value = item.getValue();
        this.creator = item.getCreator();
        this.shared = item.getIsShared();
        if (item instanceof Usable) {
            this.manufacturer = ((Usable) item).getManufacturer();
            this.model = ((Usable) item).getModel();
        }
//        if (item instanceof Serviceable) {
//            this.linkedItems = ((Serviceable) item).getLinkedItems();
//        }
    }

    public AbstractShowableEntity getEntity(){
        AbstractShowableEntity item = null;
        switch (type) {
            case "Document" -> item = new Document(this);
            case "Fastener" -> item = new Fastener(this);
            case "Manufacture" -> item = new Manufacturer(this);
            case "Consumable" -> new Consumable(this);
            case "Tool" -> new Tool(this);
            case "Part" -> new Part(this);
            case "Bike" -> new Bike(this);
        }
        return item;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
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
