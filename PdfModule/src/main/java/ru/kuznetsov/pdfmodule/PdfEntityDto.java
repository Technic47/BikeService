package ru.kuznetsov.pdfmodule;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL) //Hide null fields
public class PdfEntityDto implements Serializable {
    private String category;
    private String userName;
    private String name;
    private String description;
    private byte[] picture;
    private String link;
    private String valueName;
    private String value;
    private String manufacturer;
    private String model;
    private int amount;
    private Set<PdfEntityDto> linkedItems;

    public PdfEntityDto() {
    }

    public PdfEntityDto(String category, String userName, String name, String description, byte[] picture, String link, String valueName, String value, String manufacturer, String model, int amount, Set<PdfEntityDto> linkedItems) {
        this.category = category;
        this.userName = userName;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.link = link;
        this.valueName = valueName;
        this.value = value;
        this.manufacturer = manufacturer;
        this.model = model;
        this.amount = amount;
        this.linkedItems = linkedItems;
    }

    public PdfEntityDto(byte[] bytes) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             ObjectInputStream is = new ObjectInputStream(in)) {
            Map<String, String> fields = (Map<String, String>) is.readObject();
            this.category = fields.get("category");
            this.userName = fields.get("userName");
            this.name = fields.get("name");
            this.description = fields.get("description");
            this.picture = Base64.getDecoder().decode(fields.get("picture"));
            this.link = fields.get("link");
            this.valueName = fields.get("valueName");
            this.value = fields.get("value");
            this.manufacturer = fields.get("manufacturer");
            this.model = fields.get("model");
            this.amount = Integer.parseInt(fields.get("amount"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
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
