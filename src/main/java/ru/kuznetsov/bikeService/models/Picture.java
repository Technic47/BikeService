package ru.kuznetsov.bikeService.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.abstracts.BaseEntity;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {
//    private Long id;
    @Column(name = "name")
    private String name;

//    public Long getId() {
//        return id;
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
