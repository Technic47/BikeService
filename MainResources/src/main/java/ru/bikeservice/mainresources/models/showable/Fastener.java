package ru.bikeservice.mainresources.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;


@Entity
@Table(name = "fasteners")
public final class Fastener extends AbstractShowableEntity {
    public Fastener() {
    }

    public Fastener(Long id, String name, String description, Long picture, String link, String value, Long creator) {
        super(id, name, description, picture, link, value, creator);
    }

    public Fastener(AbstractEntityDtoNew dtoNew) {
        super(dtoNew);
    }

    @Override
    public String getValueName() {
        return "Характеристики";
    }
}
