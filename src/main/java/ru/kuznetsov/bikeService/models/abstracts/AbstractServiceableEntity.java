package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.MappedSuperclass;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;

import java.util.Set;

@MappedSuperclass
public abstract class AbstractServiceableEntity extends AbstractUsableEntity implements Serviceable {
    public AbstractServiceableEntity() {
    }

    public AbstractServiceableEntity(Long id, String name, String description, Long picture, String link, String value, Long creator, Long manufacturer, String model) {
        super(id, name, description, picture, link, value, creator, manufacturer, model);
    }

    public Set<PartEntity> getLinkedItems() {
        return null;
    }

    public void setLinkedItems(Set<PartEntity> linkedItems) {
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
