package ru.kuznetsov.bikeService.models.abstracts;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;

import java.lang.reflect.Type;
import java.util.*;

@MappedSuperclass
public abstract class AbstractServiceableEntity extends AbstractUsableEntity implements Serviceable {
    @ElementCollection(targetClass = PartEntity.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "part_item",
            joinColumns = @JoinColumn(name = "part_id"))
    private Set<PartEntity> linkedItems = new HashSet<>();

    @Column(name = "partNumber")
    protected String partNumber;
    @Deprecated
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

    public Set<PartEntity> getLinkedItems() {
        return linkedItems;
    }

    public void setLinkedItems(Set<PartEntity> linkedItems) {
        this.linkedItems = linkedItems;
    }

    @Deprecated
    public String getPartList() {
        return this.partList;
    }

    @Deprecated
    public void setPartList(String partList) {
        this.partList = partList;
    }

    @Deprecated
    public List<Long> returnPartListObject() {
        Type type = new TypeToken<ArrayList<Long>>() {
        }.getType();
        return converter.fromJson(this.partList, type);
    }

    @Deprecated
    private void updatePartListObject(List<Long> newPartList) {
        this.partList = converter.toJson(newPartList);
    }

    @Deprecated
    public void addToPartList(Serviceable item) {
        List<Long> currentPartList = this.returnPartListObject();
        currentPartList.add(item.getId());
        this.updatePartListObject(currentPartList);
    }

    @Deprecated
    public void delFromPartList(Serviceable item) {
        List<Long> currentPartList = this.returnPartListObject();
        currentPartList.remove(item.getId());
        this.updatePartListObject(currentPartList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractServiceableEntity)) return false;
        if (!super.equals(o)) return false;
        AbstractServiceableEntity that = (AbstractServiceableEntity) o;
        return Objects.equals(linkedItems, that.linkedItems) && Objects.equals(partNumber, that.partNumber) && Objects.equals(partList, that.partList) && Objects.equals(converter, that.converter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), linkedItems, partNumber, partList, converter);
    }
}
