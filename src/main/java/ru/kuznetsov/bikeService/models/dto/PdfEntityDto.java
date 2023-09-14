package ru.kuznetsov.bikeService.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Usable;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    public PdfEntityDto(Showable item, String userName, byte[] picture) {
        this.setMainFields(item, userName, picture);
    }

    public PdfEntityDto() {
    }

    public PdfEntityDto(Showable item, String userName, int amount) {
        this(item, userName, null);
        this.amount = amount;
    }

    public PdfEntityDto(Usable item, String userName, byte[] picture, String manufacturer) {
        this.setMainFields(item, userName, picture);
        this.manufacturer = manufacturer;
        this.model = item.getModel();
    }

    public PdfEntityDto(Serviceable item, String userName, byte[] picture, String manufacturer, ServiceList list) {
        this.setMainFields(item, userName, picture);
        this.manufacturer = manufacturer;
        this.model = item.getModel();
        this.linkedItems = new HashSet<>();
        list.getDocsMap().forEach((key, value) -> this.linkedItems.add(new PdfEntityDto(key, userName, value)));
        list.getConsumableMap().forEach((key, value) -> this.linkedItems.add(new PdfEntityDto(key, userName, value)));
        list.getToolMap().forEach((key, value) -> this.linkedItems.add(new PdfEntityDto(key, userName, value)));
        list.getFastenerMap().forEach((key, value) -> this.linkedItems.add(new PdfEntityDto(key, userName, value)));
    }

    public PdfEntityDto(String category, String name, String description, byte[] picture, String link, String valueName, String value, String manufacturer, String model, Set<PdfEntityDto> linkedItems) {
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

    public byte[] getBytes() {
        Map<String, String> fields = new HashMap<>();
        fields.put("category", category);
        fields.put("userName", userName);
        fields.put("name", name);
        fields.put("description", description);
        fields.put("picture", Base64.getEncoder().encodeToString(picture));
        fields.put("link", link);
        fields.put("valueName", valueName);
        fields.put("value", value);
        fields.put("manufacturer", manufacturer);
        fields.put("model", model);
        fields.put("amount", String.valueOf(amount));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(out)) {
            os.writeObject(fields);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void setMainFields(Showable item, String userName, byte[] picture) {
        this.category = item.getClass().getSimpleName();
        this.userName = userName;
        this.name = item.getName();
        this.description = item.getDescription();
        this.picture = picture;
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
