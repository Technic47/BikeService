package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;


@Entity
@Table(name = "fasteners")
public class Fastener extends AbstractShowableEntity implements Showable {
    public Fastener() {
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
