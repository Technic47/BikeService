package ru.bikeservice.mainresources.models.servicable;

import jakarta.persistence.*;
import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;
import ru.bikeservice.mainresources.models.lists.PartEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "bikes")
public final class Bike extends AbstractServiceableEntity {
    @ElementCollection(targetClass = PartEntity.class,
            fetch = FetchType.EAGER)
    @CollectionTable(name = "bike_part",
            joinColumns = @JoinColumn(name = "bike_id"))
    private Set<PartEntity> linkedParts = new HashSet<>();

    public Bike() {
    }

    public Bike(Long id, String name, String description, Long picture, String link, String value, Long creator, Long manufacturer, String model) {
        super(id, name, description, picture, link, value, creator, manufacturer, model);
    }

    public Bike(AbstractEntityDtoNew dtoNew) {
        super(dtoNew);
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
        if (!(o instanceof Bike bike)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(linkedParts, bike.linkedParts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), linkedParts);
    }
}
