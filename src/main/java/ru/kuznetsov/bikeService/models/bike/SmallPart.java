package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.JSONConverter;
import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Usable;

import javax.validation.constraints.NotEmpty;

public class SmallPart implements Serviceable, Usable {
    protected int id;
    protected int manufacturer;
    @NotEmpty(message = "Fill this field!")
    protected String model;
    protected String partNumber;
    protected String description;
    protected String serviceList;
    protected final JSONConverter<ServiceList> converterServiceList;

    public SmallPart() {
        this.converterServiceList = new JSONConverter<>();
        this.manufacturer = 0;
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

    public int getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(int manufacturer) {
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
