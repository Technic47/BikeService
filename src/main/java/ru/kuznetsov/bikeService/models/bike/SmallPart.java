package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

import javax.validation.constraints.NotEmpty;

public class SmallPart {
    private int id;
    @NotEmpty(message = "Fill this field!")
    protected Manufacturer manufacturer;
    @NotEmpty(message = "Fill this field!")
    protected String model;
    protected String partNumber;
    @NotEmpty(message = "Fill this field!")
    protected String description;
    protected ServiceList serviceList;

    public SmallPart(int id, Manufacturer manufacturer, String model) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.partNumber = "";
        this.description = "";
        this.serviceList = null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceList getServiceList() {
        return serviceList;
    }

    public void setServiceList(ServiceList serviceList) {
        this.serviceList = serviceList;
    }
}
