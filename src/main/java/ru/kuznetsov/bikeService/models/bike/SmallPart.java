package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

import javax.validation.constraints.NotEmpty;

public class SmallPart implements Serviceable{
    private int id;
    @NotEmpty(message = "Fill this field!")
    protected Manufacturer manufacturer;
    @NotEmpty(message = "Fill this field!")
    protected String model;
    protected String partNumber;
    protected String description;
    protected ServiceList serviceList;

    public SmallPart() {
        this.manufacturer = null;
        this.model = "";
        this.partNumber = "";
        this.description = "";
        this.serviceList = new ServiceList();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.model;
    }

    @Override
    public String getValue() {
        return this.partNumber;
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
