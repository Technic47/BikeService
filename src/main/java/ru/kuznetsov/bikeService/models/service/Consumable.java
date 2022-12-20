package ru.kuznetsov.bikeService.models.service;

public class Consumable implements Usable{
    private int id;
    private Manufacturer manufacturer;
    private String model;
    private String name;
    private int volume;
    private String description;

    public Consumable(int id, Manufacturer manufacturer, String model, String name, int volume, String description) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.name = name;
        this.volume = volume;
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getInfo() {
        return Integer.toString(this.volume);
    }
}
