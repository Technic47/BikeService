package ru.kuznetsov.bikeService.models.usable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;


@Entity
@Table(name = "tools")
public class Tool extends AbstractUsableEntity {
    @NotBlank(message = "Поле не должно быть пустым!")
    @Column(name = "size")
    private String size;

    public Tool() {
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String getValue() {
        return this.size;
    }

    @Override
    public String getValueName() {
        return "Размерность";
    }

    @Override
    public void setValue(String value) {
        this.size = value;
    }

    @Override
    public String toString() {
        return "Tool{" +
                "size='" + size + '\'' +
                ", model='" + model + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                ", valueName='" + valueName + '\'' +
                '}';
    }
}
