package ru.kuznetsov.bikeService.models.servicable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;

@Entity
@Table(name = "bikes")
public class Bike extends AbstractServiceableEntity {
    public Bike() {
    }

    @Override
    public String getValueName() {
        return "Заводской номер";
    }
}
