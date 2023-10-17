package ru.bikeservice.mainresources.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;

@Entity
@Table(name = "manufacturers")
public final class Manufacturer extends AbstractShowableEntity {
    public Manufacturer() {
    }

    public Manufacturer(Long id, String name, String description, Long picture, String link, String value, Long creator) {
        super(id, name, description, picture, link, value, creator);
    }

    public Manufacturer(AbstractEntityDtoNew dtoNew) {
        super(dtoNew);
    }

    public Manufacturer(EntityKafkaTransfer dtoTransfer) {
        super(dtoTransfer);
    }

    @Override
    public String getValueName() {
        return "Страна";
    }
}
