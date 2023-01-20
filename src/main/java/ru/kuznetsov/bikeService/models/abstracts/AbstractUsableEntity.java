package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import javax.validation.constraints.NotEmpty;

@MappedSuperclass
public abstract class AbstractUsableEntity extends AbstractShowableEntity{
    @NotEmpty(message = "Fill this field!")
    @Column(name = "manufacturer")
    protected Long manufacturer = 1L;
    @Column(name = "model")
    protected String model;

    public AbstractUsableEntity() {
    }

    public Long getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Long manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
