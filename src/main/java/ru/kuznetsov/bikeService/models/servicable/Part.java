package ru.kuznetsov.bikeService.models.servicable;

import com.google.gson.Gson;
import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.showable.Document;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parts")
public class Part extends AbstractServiceableEntity {
    @ManyToMany()
    @JoinTable(
            name = "part_documents",
            joinColumns = @JoinColumn(name = "part_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    List<Document> documents = new ArrayList<>();


    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }


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
