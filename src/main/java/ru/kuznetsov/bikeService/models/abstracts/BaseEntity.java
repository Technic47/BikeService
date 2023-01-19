package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.*;

@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    public BaseEntity() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
