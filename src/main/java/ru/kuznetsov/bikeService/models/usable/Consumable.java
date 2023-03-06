package ru.kuznetsov.bikeService.models.usable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;

@Entity
@Table(name = "consumables")
public class Consumable extends AbstractUsableEntity {
    @NotBlank(message = "Поле не должно быть пустым!")
    @Column(name = "volume")
    private String volume;

    public Consumable() {
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String getValue() {
        return this.volume;
    }

    @Override
    public String getValueName() {
        return "Объём";
    }

    @Override
    public void setValue(String value) {
        this.volume = value;
    }

    @Override
    public String toString() {
        return "Consumable{" +
                "volume='" + volume + '\'' +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
