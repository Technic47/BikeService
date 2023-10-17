package ru.bikeservice.mainresources.models.abstracts;

import jakarta.persistence.MappedSuperclass;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.servicable.Serviceable;

import java.util.Set;

@MappedSuperclass
public abstract class AbstractServiceableEntity extends AbstractUsableEntity implements Serviceable {
    public AbstractServiceableEntity() {
    }

    public AbstractServiceableEntity(Long id, String name, String description, Long picture, String link, String value, Long creator, Long manufacturer, String model) {
        super(id, name, description, picture, link, value, creator, manufacturer, model);
    }

    public AbstractServiceableEntity(AbstractEntityDtoNew dtoNew) {
        super(dtoNew);
    }

    public AbstractServiceableEntity(EntityKafkaTransfer dtoTransfer) {
        super(dtoTransfer);
    }

    abstract public Set<PartEntity> getLinkedItems();

    abstract public void setLinkedItems(Set<PartEntity> linkedItems);
}
