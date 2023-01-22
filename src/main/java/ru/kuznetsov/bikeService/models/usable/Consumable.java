package ru.kuznetsov.bikeService.models.usable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "consumables")
public class Consumable extends AbstractUsableEntity {
    @NotEmpty(message = "Fill this field!")
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
    public void setValue(String value) {
        this.volume = value;
    }
}
