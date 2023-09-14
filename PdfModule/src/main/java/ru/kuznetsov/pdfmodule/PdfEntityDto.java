package ru.kuznetsov.pdfmodule;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Base64;
import java.util.Map;

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
    private Map<String, String[]> linkedItems;

    public PdfEntityDto() {
    }


    public PdfEntityDto(byte[] bytes) {
        Map<String, String> fields = (Map<String, String>) ByteUtils.fromBytes(bytes);
        setMainFields(fields);
    }

    private void setMainFields(Map<String, String> fields) {
        this.category = fields.getOrDefault("category", null);
        this.userName = fields.getOrDefault("userName", null);
        this.name = fields.getOrDefault("name", null);
        this.description = fields.getOrDefault("description", null);
        this.picture = Base64.getDecoder().decode(fields.get("picture"));
        this.link = fields.getOrDefault("link", null);
        this.valueName = fields.getOrDefault("valueName", null);
        this.value = fields.getOrDefault("value", null);
        this.manufacturer = fields.getOrDefault("manufacturer", null);
        this.model = fields.getOrDefault("model", null);
        this.amount = Integer.parseInt(fields.get("amount"));
        this.linkedItems = (Map<String, String[]>) ByteUtils.fromBytes(Base64.getDecoder().decode(fields.get("linkedItems")));
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

    public Map<String, String[]> getLinkedItems() {
        return linkedItems;
    }

    public void setLinkedItems(Map<String, String[]> linkedItems) {
        this.linkedItems = linkedItems;
    }

    public String getCredentials() {
        return this.valueName + " - " + this.value;
    }
}
