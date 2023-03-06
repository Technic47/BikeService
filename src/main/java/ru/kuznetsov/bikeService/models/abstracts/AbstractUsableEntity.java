package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import ru.kuznetsov.bikeService.models.usable.Usable;

import java.util.Objects;

@MappedSuperclass
public abstract class AbstractUsableEntity extends AbstractShowableEntity implements Usable {
    @NotBlank(message = "Поле не должно быть пустым!")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractUsableEntity)) return false;
        if (!super.equals(o)) return false;
        AbstractUsableEntity that = (AbstractUsableEntity) o;
        return Objects.equals(manufacturer, that.manufacturer) && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), manufacturer, model);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
