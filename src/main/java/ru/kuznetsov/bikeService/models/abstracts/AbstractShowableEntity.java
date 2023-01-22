package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.showable.Showable;

import javax.validation.constraints.NotEmpty;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractShowableEntity implements Showable {

    protected Long id;
    @NotEmpty(message = "Fill this field!")
    @Column(name = "name")
    protected String name;
    @Column(name = "description")
    protected String description;
    @Column(name = "picture")
    protected Long picture;
    @Column(name = "link")
    protected String link;
    @Transient
    protected String value;

    public AbstractShowableEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPicture() {
        return picture;
    }

    public void setPicture(Long picture) {
        this.picture = picture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
