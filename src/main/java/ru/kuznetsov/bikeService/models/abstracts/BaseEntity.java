package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.*;

//@Entity
@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED)
//@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity {

    protected Long id;

    public BaseEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
