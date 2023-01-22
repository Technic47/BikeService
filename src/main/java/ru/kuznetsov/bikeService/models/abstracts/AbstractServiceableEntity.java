package ru.kuznetsov.bikeService.models.abstracts;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Showable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractServiceableEntity extends AbstractUsableEntity implements Serviceable{
    @Column(name = "partNumber")
    protected String partNumber;

    @Column(name = "serviceList")

    protected String serviceList;

    @Column(name = "partList")
    protected String partList;
    @Transient
    protected Gson converter;

    public AbstractServiceableEntity() {
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
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
        Type type = new TypeToken<ArrayList<Long>>() {}.getType();
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
