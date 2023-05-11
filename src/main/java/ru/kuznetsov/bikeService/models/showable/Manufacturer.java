package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

@Entity
@Table(name = "manufacturers")
public class Manufacturer extends AbstractShowableEntity {
    public Manufacturer() {
    }

    public Manufacturer(Long id, String name, String description, Long picture, String link, String value, Long creator) {
        super(id, name, description, picture, link, value, creator);
    }

    @Override
    public String getValueName() {
        return "Страна";
    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
