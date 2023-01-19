package ru.kuznetsov.bikeService.models.bike;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.abstracts.BaseEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parts")
public class Part extends BaseEntity implements Serviceable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @NotEmpty(message = "Fill this field!")
    @Column(name = "name")
    protected String name;
    @Column(name = "description")
    protected String description;
    //    @Column(name="link")
//    protected String link;
    @Column(name = "picture")
    protected Long picture;
    @Transient
    protected String value;
    @NotEmpty(message = "Fill this field!")
    @Column(name = "manufacturer")
    protected Long manufacturer;
    @Column(name = "model")
    protected String model;
    @Column(name = "partNumber")
    protected String partNumber;
    @Column(name = "serviceList")
    protected String serviceList;
    @Column(name = "partList")
    protected String partList;
    @Transient
    protected final Gson converter;

    public Part() {
        this.manufacturer = 1L;
        this.model = "";
        this.partNumber = "";
        this.description = "";
        this.converter = new Gson();
        this.serviceList = this.converter.toJson(new ServiceList());
        this.partList = this.converter.toJson(new ArrayList<Integer>());
    }

//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public Long getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(Long manufacturer) {
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

    @Override
    public Long getPicture() {
        return picture;
    }

    public void setPicture(Long picture) {
        this.picture = picture;
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

    public String getPartList() {
        return this.partList;
    }

    public void setPartList(String partList) {
        this.partList = partList;
    }

    public List<Long> returnPartListObject() {
        Type type = new TypeToken<ArrayList<Long>>() {
        }.getType();
        return converter.fromJson(this.partList, type);
    }

    private void updatePartListObject(List<Long> newPartList) {
        this.partList = converter.toJson(newPartList);
    }

    public void addToPartList(Serviceable item) {
        List<Long> currentPartList = this.returnPartListObject();
        currentPartList.add(item.getId());
        this.updatePartListObject(currentPartList);
    }

    public void delFromPartList(Serviceable item) {
        List<Long> currentPartList = this.returnPartListObject();
        currentPartList.remove(item.getId().intValue());
        this.updatePartListObject(currentPartList);
    }
}
