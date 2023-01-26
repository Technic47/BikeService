package ru.kuznetsov.bikeService.models;

import jakarta.persistence.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picture)) return false;
        Picture picture = (Picture) o;
        return id.equals(picture.id) && name.equals(picture.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
