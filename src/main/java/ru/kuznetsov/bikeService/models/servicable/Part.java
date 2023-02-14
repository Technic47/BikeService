package ru.kuznetsov.bikeService.models.servicable;

import com.google.gson.Gson;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;

import java.util.ArrayList;

@Entity
@Table(name = "parts")
public class Part extends AbstractServiceableEntity {
    public Part() {
        this.converter = new Gson();
        this.serviceList = this.converter.toJson(new ServiceList());
        this.partList = this.converter.toJson(new ArrayList<Integer>());
    }


    @Override
    public String getValue() {
        return this.partNumber;
    }

    @Override
    public String getValueName() {
        return "Part number";
    }

    @Override
    public void setValue(String value) {
        this.partNumber = value;
    }
}
