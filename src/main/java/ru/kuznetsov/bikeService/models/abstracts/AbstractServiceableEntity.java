//package ru.kuznetsov.bikeService.models.abstracts;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import jakarta.persistence.Column;
//import jakarta.persistence.MappedSuperclass;
//import jakarta.persistence.Transient;
//import ru.kuznetsov.bikeService.models.Showable;
//import ru.kuznetsov.bikeService.models.bike.Serviceable;
//import ru.kuznetsov.bikeService.models.lists.ServiceList;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//@MappedSuperclass
//public class AbstractServiceableEntity extends AbstractUsableEntity implements Serviceable {
//    @Column(name="partNumber")
//    protected String partNumber;
//    @Column(name="serviceList")
//    protected String serviceList;
//    @Column(name="partList")
//    protected String partList;
//    @Transient
//    protected final Gson converter;
//
//    public AbstractServiceableEntity() {
//        this.converter = new Gson();
//        this.serviceList = this.converter.toJson(new ServiceList());
//        this.partList = this.converter.toJson(new ArrayList<Integer>());
//    }
//
//    public String getPartNumber() {
//        return partNumber;
//    }
//
//    public void setPartNumber(String partNumber) {
//        this.partNumber = partNumber;
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
//}
