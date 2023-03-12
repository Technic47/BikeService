package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.MappedSuperclass;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;

import java.util.Set;

@MappedSuperclass
public abstract class AbstractServiceableEntity extends AbstractUsableEntity implements Serviceable {
    public AbstractServiceableEntity() {
    }


    public Set<PartEntity> getLinkedItems() {
        return null;
    }

    public void setLinkedItems(Set<PartEntity> linkedItems) {
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
