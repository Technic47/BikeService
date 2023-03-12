package ru.kuznetsov.bikeService.models.servicable;

import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.PartEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "bikes")
public class Bike extends AbstractServiceableEntity {
    @ElementCollection(targetClass = PartEntity.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "bike_part",
            joinColumns = @JoinColumn(name = "bike_id"))
    private Set<PartEntity> linkedParts = new HashSet<>();
    public Bike() {
    }

    @Override
    public Set<PartEntity> getLinkedItems() {
        return linkedParts;
    }

    @Override
    public void setLinkedItems(Set<PartEntity> linkedItems) {
        this.linkedParts = linkedItems;
    }

    @Override
    public String getValueName() {
        return "Заводской номер";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bike)) return false;
        if (!super.equals(o)) return false;
        Bike bike = (Bike) o;
        return Objects.equals(linkedParts, bike.linkedParts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), linkedParts);
    }
}
