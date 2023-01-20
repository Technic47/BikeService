package ru.kuznetsov.bikeService.models.servicable;

import com.google.gson.Gson;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;

import java.util.ArrayList;

@Entity
@Table(name = "parts")
public class Part extends AbstractServiceableEntity implements Serviceable {
    public Part() {
        this.converter = new Gson();
        this.serviceList = this.converter.toJson(new ServiceList());
        this.partList = this.converter.toJson(new ArrayList<Integer>());
    }


    @Override
    public String getValue() {
        return this.partNumber;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

//    public Long getManufacturer() {
//        return this.manufacturer;
//    }
//
//    public void setManufacturer(Long manufacturer) {
//        this.manufacturer = manufacturer;
//    }
//
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
//    public Long getPicture() {
//        return picture;
//    }
//
//    public void setPicture(Long picture) {
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
//    public List<Long> returnPartListObject() {
//        Type type = new TypeToken<ArrayList<Long>>() {
//        }.getType();
//        return converter.fromJson(this.partList, type);
//    }
//
//    private void updatePartListObject(List<Long> newPartList) {
//        this.partList = converter.toJson(newPartList);
//    }
//
//    public void addToPartList(Serviceable item) {
//        List<Long> currentPartList = this.returnPartListObject();
//        currentPartList.add(item.getId());
//        this.updatePartListObject(currentPartList);
//    }
//
//    public void delFromPartList(Serviceable item) {
//        List<Long> currentPartList = this.returnPartListObject();
//        currentPartList.remove(item.getId().intValue());
//        this.updatePartListObject(currentPartList);
//    }
}
