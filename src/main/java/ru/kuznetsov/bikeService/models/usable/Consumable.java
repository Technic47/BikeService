package ru.kuznetsov.bikeService.models.usable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;

@Entity
@Table(name = "consumables")
public class Consumable extends AbstractUsableEntity {
    public Consumable() {
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
