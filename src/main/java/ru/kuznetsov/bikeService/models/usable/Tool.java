package ru.kuznetsov.bikeService.models.usable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;


@Entity
@Table(name = "tools")
public class Tool extends AbstractUsableEntity {

    public Tool() {
    }

    @Override
    public String getValueName() {
        return "Размерность";
    }

    @Override
    public String toString() {
        return "Tool{" +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
