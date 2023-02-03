package ru.kuznetsov.bikeService.models.usable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;

import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "tools")
public class Tool extends AbstractUsableEntity {
    @NotEmpty(message = "Fill this field!")
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
        return "Size";
    }

    @Override
    public void setValue(String value) {
        this.size = value;
    }
}
