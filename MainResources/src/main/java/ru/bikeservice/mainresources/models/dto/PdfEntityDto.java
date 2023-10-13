package ru.bikeservice.mainresources.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ru.bikeservice.mainresources.models.lists.ServiceList;
import ru.bikeservice.mainresources.models.showable.Fastener;
import ru.bikeservice.mainresources.models.showable.Showable;
import ru.bikeservice.mainresources.models.usable.Consumable;
import ru.bikeservice.mainresources.models.usable.Tool;
import ru.bikeservice.mainresources.utils.ByteUtils;

import java.io.Serializable;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
    private Map<String, String[]> linkedItems;

    public PdfEntityDto(Showable item, String userName, byte[] picture) {
        this.setMainFields(item, userName, picture);
    }

    private String[] formToolArray(Map<Tool, Integer> map) {
        int size = map.size();
        String[] stringArray = new String[size];
        int index = 0;

        for (Map.Entry<Tool, Integer> showableEntry : map.entrySet()) {
            stringArray[index] = showableEntry.getKey().getCredentials();
            index++;
        }
        return stringArray;
    }

    private String[] formConsumableArray(Map<Consumable, Integer> map) {
        int size = map.size();
        String[] stringArray = new String[size];
        int index = 0;

        for (Map.Entry<Consumable, Integer> showableEntry : map.entrySet()) {
            stringArray[index] = showableEntry.getKey().getName()
                    + ", " + showableEntry.getValue();
            index++;
        }
        return stringArray;
    }

    private String[] formFastenerArray(Map<Fastener, Integer> map) {
        int size = map.size();
        String[] stringArray = new String[size];
        int index = 0;

        for (Map.Entry<Fastener, Integer> showableEntry : map.entrySet()) {
            stringArray[index] = showableEntry.getKey().getName()
                    + ", " + showableEntry.getValue();
            index++;
        }
        return stringArray;
    }

    public byte[] getBytes() {
        Map<String, String> fields = new HashMap<>();
        fields.put("category", category);
        fields.put("name", name);
        fields.put("valueName", valueName);
        fields.put("value", value);
        fields.put("amount", String.valueOf(amount));
        fields.put("userName", userName);
        fields.put("description", description);
        fields.put("link", link);
        fields.put("manufacturer", manufacturer);
        fields.put("model", model);
        fields.put("picture", Base64.getEncoder().encodeToString(picture));
        fields.put("linkedItems", Base64.getEncoder().encodeToString(ByteUtils.toBytes(linkedItems)));
        return ByteUtils.toBytes(fields);
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

    public Map<String, String[]> getLinkedItems() {
        return linkedItems;
    }

    public void setLinkedItems(ServiceList list) {
        this.linkedItems = new HashMap<>();
        this.linkedItems.put("tools", formToolArray(list.getToolMap()));
        this.linkedItems.put("consumables", formConsumableArray(list.getConsumableMap()));
        this.linkedItems.put("fasteners", formFastenerArray(list.getFastenerMap()));
    }
}
