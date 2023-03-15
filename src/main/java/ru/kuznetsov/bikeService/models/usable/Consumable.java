package ru.kuznetsov.bikeService.models.usable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;

@Entity
@Table(name = "consumables")
public class Consumable extends AbstractUsableEntity {
    public Consumable() {
    }

    public Consumable(Long id, String name, String description, Long picture, String link, String value, Long creator, Long manufacturer, String model) {
        super(id, name, description, picture, link, value, creator, manufacturer, model);
    }

    @Override
    public String getValueName() {
        return "Объём";
    }

    @Override
    public String toString() {
        return "Consumable{" +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
