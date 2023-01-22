package ru.kuznetsov.bikeService.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pictures")
public class Picture {

    private Long id;
    @Column(name = "name")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
