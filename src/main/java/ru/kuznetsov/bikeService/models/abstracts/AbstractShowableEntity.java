package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import ru.kuznetsov.bikeService.models.Showable;

import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractShowableEntity implements Serializable, Showable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
