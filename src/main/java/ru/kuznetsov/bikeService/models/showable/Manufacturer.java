package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

@Entity
@Table(name = "manufacturers")
public class Manufacturer extends AbstractShowableEntity implements Showable {
    @Column(name = "country")
    private String country;

    public Manufacturer() {
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String getValue() {
        return this.country;
    }

    @Override
    public void setValue(String value) {
        this.country = value;
    }
}
