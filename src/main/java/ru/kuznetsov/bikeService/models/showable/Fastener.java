package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "fasteners")
public class Fastener extends AbstractShowableEntity implements Showable {
    @NotEmpty(message = "Fill this field!")
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
        return "Specs";
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }
}
