package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDtoNew;


@Entity
@Table(name = "fasteners")
public class Fastener extends AbstractShowableEntity {
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
