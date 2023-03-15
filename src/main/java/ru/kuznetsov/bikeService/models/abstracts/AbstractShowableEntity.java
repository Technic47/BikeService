package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.kuznetsov.bikeService.models.showable.Showable;

import java.util.Objects;

@MappedSuperclass
public abstract class AbstractShowableEntity implements Showable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @NotBlank(message = "Поле не должно быть пустым!")
    @Size(min = 1, max = 100)
    @Column(name = "name")
    protected String name;
    @Column(name = "description")
    @NotBlank(message = "Поле не должно быть пустым!")
    @Size(min = 1, max = 255)
    protected String description;
    @Column(name = "picture")
    protected Long picture;
    @Size(max = 100)
    @Column(name = "link")
    protected String link;
    @NotBlank(message = "Поле не должно быть пустым!")
    @Size(min = 1, max = 200)
    @Column(name = "value")
    protected String value;
    @Transient
    protected String valueName;
    @Column(name = "creator")
    protected Long creator;

    public AbstractShowableEntity() {
    }

    public AbstractShowableEntity(Long id, String name, String description, Long picture, String link, String value, Long creator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.link = link;
        this.value = value;
        this.creator = creator;
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", value='" + this.getValue() + '\'' +
                ", valueName='" + this.getValueName() + '\'' +
                '}';
    }

    public String getCredentials() {
        return name +
                ", " + this.getValueName() +
                ": " + this.getValue();
    }
}
