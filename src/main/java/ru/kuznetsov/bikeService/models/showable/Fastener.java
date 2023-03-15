package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;


@Entity
@Table(name = "fasteners")
public class Fastener extends AbstractShowableEntity implements Showable {
    public Fastener() {
    }

    public Fastener(Long id, String name, String description, Long picture, String link, String value, Long creator) {
        super(id, name, description, picture, link, value, creator);
    }

    @Override
    public String getValueName() {
        return "Характеристики";
    }

    @Override
    public String toString() {
        return "Fastener{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
