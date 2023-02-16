package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
public abstract class AbstractServiceableEntity extends AbstractUsableEntity implements Serviceable {
    @ElementCollection(targetClass = PartEntity.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "part_item",
            joinColumns = @JoinColumn(name = "part_id"))
    private Set<PartEntity> linkedItems = new HashSet<>();

    @Column(name = "partNumber")
    protected String partNumber;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractServiceableEntity)) return false;
        if (!super.equals(o)) return false;
        AbstractServiceableEntity that = (AbstractServiceableEntity) o;
        return Objects.equals(linkedItems, that.linkedItems) && Objects.equals(partNumber, that.partNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), linkedItems, partNumber);
    }
}
