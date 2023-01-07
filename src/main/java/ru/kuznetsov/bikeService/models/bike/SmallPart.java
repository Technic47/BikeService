package ru.kuznetsov.bikeService.models.bike;

import com.google.gson.Gson;
import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Usable;

import javax.validation.constraints.NotEmpty;

public class SmallPart implements Serviceable, Usable {
    protected int id;
    protected int manufacturer;
    @NotEmpty(message = "Fill this field!")
    protected String name;
    @NotEmpty(message = "Fill this field!")
    protected String model;
    protected String partNumber;
    protected String description;
    protected String serviceList;
    protected final Gson converter;

    public SmallPart() {
        this.manufacturer = 1;
        this.model = "";
        this.partNumber = "";
        this.description = "";
        this.converter = new Gson();
        this.serviceList = this.converter.toJson(new ServiceList());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ServiceList returnServiceListObject() {
        return converter.fromJson(this.serviceList, ServiceList.class);
    }

    public String getServiceList() {
        return this.serviceList;
    }

    public void setServiceList(String newServiceList) {
        this.serviceList = newServiceList;
    }

    private void updateServiceListObject(ServiceList newList) {
        this.serviceList = converter.toJson(newList);
    }

    public void addToServiceList(Showable item) {
        ServiceList currentServiceList = this.returnServiceListObject();
        currentServiceList.addToList(item);
        this.updateServiceListObject(currentServiceList);
    }

    public void delFromServiceList(Showable item) {
        ServiceList currentServiceList = this.returnServiceListObject();
        currentServiceList.delFromList(item);
        this.updateServiceListObject(currentServiceList);
    }
}
