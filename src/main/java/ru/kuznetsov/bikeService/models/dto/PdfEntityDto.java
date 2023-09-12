package ru.kuznetsov.bikeService.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Usable;

import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PdfEntityDto {
    private String category;
    private String userName;
    private String name;
    private String description;
    private Long picture;
    private String link;
    private String valueName;
    private String value;
    private String manufacturer;
    private String model;
    private int amount;
    private Set<PdfEntityDto> linkedItems;

    public PdfEntityDto() {
    }

    public PdfEntityDto(Showable item, String userName) {
        this.setMainFields(item, userName);
    }

    public PdfEntityDto(Showable item, String userName, int amount) {
        this(item, userName);
        this.amount = amount;
    }

    public PdfEntityDto(Usable item, String userName, String manufacturer) {
        this.setMainFields(item, userName);
        this.manufacturer = manufacturer;
        this.model = item.getModel();
    }

    public PdfEntityDto(Serviceable item, String userName, String manufacturer, ServiceList list) {
        this.setMainFields(item, userName);
        this.manufacturer = manufacturer;
        this.model = item.getModel();
        this.linkedItems = new HashSet<>();
        list.getDocsMap().forEach((key, value) -> this.linkedItems.add(new PdfEntityDto(key, userName, value)));
        list.getConsumableMap().forEach((key, value) -> this.linkedItems.add(new PdfEntityDto(key, userName, value)));
        list.getToolMap().forEach((key, value) -> this.linkedItems.add(new PdfEntityDto(key, userName, value)));
        list.getFastenerMap().forEach((key, value) -> this.linkedItems.add(new PdfEntityDto(key, userName, value)));
    }

    public PdfEntityDto(String category, String name, String description, Long picture, String link, String valueName, String value, String manufacturer, String model, Set<PdfEntityDto> linkedItems) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.link = link;
        this.valueName = valueName;
        this.value = value;
        this.manufacturer = manufacturer;
        this.model = model;
        this.linkedItems = linkedItems;
    }

    private void setMainFields(Showable item, String userName) {
        this.category = item.getClass().getSimpleName();
        this.userName = userName;
        this.name = item.getName();
        this.description = item.getDescription();
        this.picture = item.getPicture();
        this.link = item.getLink();
        this.valueName = item.getValueName();
        this.value = item.getValue();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Set<PdfEntityDto> getLinkedItems() {
        return linkedItems;
    }

    public void setLinkedItems(Set<PdfEntityDto> linkedItems) {
        this.linkedItems = linkedItems;
    }

    public String getCredentials() {
        return this.valueName + " - " + this.value;
    }
}
