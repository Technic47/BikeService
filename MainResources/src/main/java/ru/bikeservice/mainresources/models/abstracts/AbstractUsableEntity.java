package ru.bikeservice.mainresources.models.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.usable.Usable;

import java.util.Objects;

@MappedSuperclass
public abstract class AbstractUsableEntity extends AbstractShowableEntity implements Usable {
    @NotNull(message = "Поле не должно быть пустым!")
    @Column(name = "manufacturer")
    protected Long manufacturer = 1L;
    @NotBlank(message = "Поле не должно быть пустым!")
    @Size(min = 1, max = 100)
    @Column(name = "model")
    protected String model;


    public AbstractUsableEntity() {
    }

    public AbstractUsableEntity(Long id, String name, String description, Long picture, String link, String value, Long creator, Long manufacturer, String model) {
        super(id, name, description, picture, link, value, creator);
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public AbstractUsableEntity(AbstractEntityDtoNew dtoNew) {
        super(dtoNew);
        this.manufacturer = dtoNew.getManufacturer();
        this.model = dtoNew.getModel();
    }

    public AbstractUsableEntity(EntityKafkaTransfer dtoTransfer) {
        super(dtoTransfer);
        this.manufacturer = dtoTransfer.getManufacturer();
        this.model = dtoTransfer.getModel();
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
        if (!(o instanceof AbstractUsableEntity that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(manufacturer, that.manufacturer) && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), manufacturer, model);
    }
}
