package ru.kuznetsov.bikeService.models.abstracts;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class AbstractServiceableEntity extends AbstractUsableEntity implements Serviceable {
    @ElementCollection(targetClass = PartEntity.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "part_item",
            joinColumns = @JoinColumn(name = "part_id"))
    private List<PartEntity> linkedItems = new ArrayList<>();


    //    @ManyToMany()
//    @JoinTable(
//            name = "part_documents",
//            joinColumns = @JoinColumn(name = "part_id"),
//            inverseJoinColumns = @JoinColumn(name = "document_id")
//    )
//    protected List<Document> documents = new ArrayList<>();
//
//    @ManyToMany()
//    @JoinTable(
//            name = "part_fasteners",
//            joinColumns = @JoinColumn(name = "part_id"),
//            inverseJoinColumns = @JoinColumn(name = "fastener_id")
//    )
//    protected List<Fastener> fasteners = new ArrayList<>();
//
//    @ManyToMany()
//    @JoinTable(
//            name = "part_tools",
//            joinColumns = @JoinColumn(name = "part_id"),
//            inverseJoinColumns = @JoinColumn(name = "tool_id")
//    )
//    protected List<Tool> tools = new ArrayList<>();
//
//    @ManyToMany()
//    @JoinTable(
//            name = "part_consumables",
//            joinColumns = @JoinColumn(name = "part_id"),
//            inverseJoinColumns = @JoinColumn(name = "consumable_id")
//    )
//    protected List<Consumable> consumables = new ArrayList<>();
//

    @Column(name = "partNumber")
    protected String partNumber;
    //    @Column(name = "serviceList")
//    protected String serviceList;
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

    public List<PartEntity> getLinkedItems() {
        return linkedItems;
    }

    public void setLinkedItems(List<PartEntity> linkedItems) {
        this.linkedItems = linkedItems;
    }

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
        currentPartList.remove(item.getId());
        this.updatePartListObject(currentPartList);
    }

//    public List<Document> getDocuments() {
//        return documents;
//    }
//
//    public void setDocuments(List<Document> documents) {
//        this.documents = documents;
//    }
//
//    public List<Fastener> getFasteners() {
//        return fasteners;
//    }
//
//    public void setFasteners(List<Fastener> fasteners) {
//        this.fasteners = fasteners;
//    }
//
//    public List<Tool> getTools() {
//        return tools;
//    }
//
//    public void setTools(List<Tool> tools) {
//        this.tools = tools;
//    }
//
//    public List<Consumable> getConsumables() {
//        return consumables;
//    }
//
//    public void setConsumables(List<Consumable> consumables) {
//        this.consumables = consumables;
//    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof AbstractServiceableEntity)) return false;
//        if (!super.equals(o)) return false;
//        AbstractServiceableEntity that = (AbstractServiceableEntity) o;
//        return Objects.equals(partNumber, that.partNumber) && serviceList.equals(that.serviceList) && partList.equals(that.partList) && Objects.equals(converter, that.converter);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), partNumber, serviceList, partList, converter);
//    }
}
