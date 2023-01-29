package ru.kuznetsov.bikeService.models.servicable;

import com.google.gson.Gson;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;

import java.util.ArrayList;

@Entity
@Embeddable
@Table(name = "bikes")
public class Bike extends AbstractServiceableEntity {
    public Bike() {
        this.converter = new Gson();
        this.serviceList = this.converter.toJson(new ServiceList());
        this.partList = this.converter.toJson(new ArrayList<Integer>());
    }


    @Override
    public String getValue() {
        return this.partNumber;
    }

    @Override
    public void setValue(String value) {
        this.partNumber = value;
    }
}
