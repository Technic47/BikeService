package ru.kuznetsov.bikeService.models.bike;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.Entity;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Usable;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Part extends AbstractServiceableEntity {

//    protected int manufacturer;
//    @NotEmpty(message = "Fill this field!")
//    protected String name;
//    @NotEmpty(message = "Fill this field!")
//    protected String model;
//    protected String partNumber;
//    protected String description;
//    protected int picture;
//    private String value;
//    protected String serviceList;
//    protected String partList;
//
//    protected final Gson converter;

    public Part() {
//        this.manufacturer = 1;
//        this.model = "";
//        this.partNumber = "";
//        this.description = "";
//        this.converter = new Gson();
//        this.serviceList = this.converter.toJson(new ServiceList());
//        this.partList = this.converter.toJson(new ArrayList<Integer>());
    }


//    @Override
//    public String getName() {
//        return this.name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    @Override
    public String getValue() {
        return this.partNumber;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

//    public int getManufacturer() {
//        return this.manufacturer;
//    }
//
//    public void setManufacturer(int manufacturer) {
//        this.manufacturer = manufacturer;
//    }

//    public String getModel() {
//        return model;
//    }
//
//    public void setModel(String model) {
//        this.model = model;
//    }

//    public String getPartNumber() {
//        return partNumber;
//    }
//
//    public void setPartNumber(String partNumber) {
//        this.partNumber = partNumber;
//    }

//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

//    @Override
//    public int getPicture() {
//        return picture;
//    }
//
//    public void setPicture(int picture) {
//        this.picture = picture;
//    }

//    public ServiceList returnServiceListObject() {
//        return converter.fromJson(this.serviceList, ServiceList.class);
//    }
//
//    public String getServiceList() {
//        return this.serviceList;
//    }
//
//    public void setServiceList(String newServiceList) {
//        this.serviceList = newServiceList;
//    }
//
//    private void updateServiceListObject(ServiceList newList) {
//        this.serviceList = converter.toJson(newList);
//    }
//
//    public void addToServiceList(Showable item) {
//        ServiceList currentServiceList = this.returnServiceListObject();
//        currentServiceList.addToList(item);
//        this.updateServiceListObject(currentServiceList);
//    }
//
//    public void delFromServiceList(Showable item) {
//        ServiceList currentServiceList = this.returnServiceListObject();
//        currentServiceList.delFromList(item);
//        this.updateServiceListObject(currentServiceList);
//    }
//
//    public String getPartList() {
//        return this.partList;
//    }
//
//    public void setPartList(String partList) {
//        this.partList = partList;
//    }
//
//    public List<Integer> returnPartListObject() {
//        Type type = new TypeToken<ArrayList<Integer>>() {
//        }.getType();
//        return converter.fromJson(this.partList, type);
//    }
//
//    private void updatePartListObject(List<Integer> newPartList) {
//        this.partList = converter.toJson(newPartList);
//    }
//
//    public void addToPartList(Serviceable item) {
//        List<Integer> currentPartList = this.returnPartListObject();
//        currentPartList.add(Math.toIntExact(item.getId()));
//        this.updatePartListObject(currentPartList);
//    }
//
//    public void delFromPartList(Serviceable item) {
//        List<Integer> currentPartList = this.returnPartListObject();
//        currentPartList.remove(item.getId().intValue());
//        this.updatePartListObject(currentPartList);
//    }
}
