package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.JSONConverter;
import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

import javax.validation.constraints.NotEmpty;

public class SmallPart implements Serviceable {
    protected int id;
    protected String manufacturer;
    @NotEmpty(message = "Fill this field!")
    protected String model;
    protected String partNumber;
    protected String description;
    protected String serviceList;
    protected final JSONConverter<Manufacturer> converterManufacturer;
    protected final JSONConverter<ServiceList> converterServiceList;

    public SmallPart() {
        this.converterManufacturer = new JSONConverter<>();
        this.converterServiceList = new JSONConverter<>();
        this.manufacturer = "";
        this.model = "";
        this.partNumber = "";
        this.description = "";
        ServiceList newServiceList = new ServiceList();
        this.serviceList = this.converterServiceList.toJson(newServiceList);
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
        return converterManufacturer.fromJson(this.manufacturer);
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = converterManufacturer.toJson(manufacturer);
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
        return converterServiceList.fromJson(this.serviceList);
    }

    private void setServiceList(ServiceList newList) {
        this.serviceList = converterServiceList.toJson(newList);
    }

    public void addToServiceList(Showable item) {
        ServiceList currentServiceList = this.getServiceList();
        currentServiceList.addToList(item);
        this.setServiceList(currentServiceList);
    }
}
