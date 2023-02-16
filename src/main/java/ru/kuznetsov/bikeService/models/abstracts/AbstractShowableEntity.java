package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.*;
import ru.kuznetsov.bikeService.models.showable.Showable;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractShowableEntity implements Showable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Transient
    protected String valueName;
    @Column(name = "creator")
    protected Long creator;

    public AbstractShowableEntity() {
    }


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

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractShowableEntity)) return false;
        AbstractShowableEntity that = (AbstractShowableEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(picture, that.picture) && Objects.equals(link, that.link) && Objects.equals(value, that.value) && Objects.equals(valueName, that.valueName) && Objects.equals(creator, that.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, picture, link, value, valueName, creator);
    }
}
