package ru.kuznetsov.bikeService.models.servicable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;

@Entity
@Table(name = "parts")
public class Part extends AbstractServiceableEntity {
    public Part() {
    }

    @Override
    public String getValueName() {
        return "Заводской номер";
    }
}
