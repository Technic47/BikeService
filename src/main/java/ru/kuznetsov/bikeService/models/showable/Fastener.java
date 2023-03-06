package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;


@Entity
@Table(name = "fasteners")
public class Fastener extends AbstractShowableEntity implements Showable {
    @NotBlank(message = "Поле не должно быть пустым!")
    @Column(name = "specs")
    private String specs;

    public Fastener() {
    }

    @Override
    public String getValue() {
        return this.specs;
    }

    @Override
    public void setValue(String value) {
        this.specs = value;
    }

    @Override
    public String getValueName() {
        return "Характеристики";
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    @Override
    public String toString() {
        return "Fastener{" +
                "specs='" + specs + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
