package ru.bikeservice.mainresources.models.servicable;

import jakarta.persistence.*;
import ru.bikeservice.mainresources.models.abstracts.AbstractServiceableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.lists.PartEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "parts")
public final class Part extends AbstractServiceableEntity {
    @ElementCollection(targetClass = PartEntity.class,
            fetch = FetchType.EAGER)
    @CollectionTable(name = "part_item",
            joinColumns = @JoinColumn(name = "part_id"))
    private Set<PartEntity> linkedItems = new HashSet<>();

    public Part() {
    }

    public Part(Long id, String name, String description, Long picture, String link, String value, Long creator, Long manufacturer, String model) {
        super(id, name, description, picture, link, value, creator, manufacturer, model);
    }

    public Part(AbstractEntityDtoNew dtoNew) {
        super(dtoNew);
    }

    public Part(EntityKafkaTransfer dtoTransfer) {
        super(dtoTransfer);
        this.linkedItems = dtoTransfer.getLinkedItems();
    }

    @Override
    public Set<PartEntity> getLinkedItems() {
        return linkedItems;
    }

    @Override
    public void setLinkedItems(Set<PartEntity> linkedItems) {
        this.linkedItems = linkedItems;
    }

    @Override
    public String getValueName() {
        return "Заводской номер";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Part part)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(linkedItems, part.linkedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), linkedItems);
    }
}
