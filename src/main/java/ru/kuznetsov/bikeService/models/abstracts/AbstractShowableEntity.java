//package ru.kuznetsov.bikeService.models.abstracts;
//
//import jakarta.persistence.*;
//import ru.kuznetsov.bikeService.models.Showable;
//
//import javax.validation.constraints.NotEmpty;
//
//
////@MappedSuperclass
////@Entity
////@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//public abstract class AbstractShowableEntity implements Showable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    protected Long id;
//    @NotEmpty(message = "Fill this field!")
//    @Column(name="name")
//    protected String name;
//    @Column(name="description")
//    protected String description;
//    @Column(name="link")
//    protected String link;
//    @Column(name="picture")
//    protected int picture;
//    @Transient
//    protected String value;
//
//    public AbstractShowableEntity() {
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public String getValue() {
//        return this.value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getLink() {
//        return link;
//    }
//
//    public void setLink(String link) {
//        this.link = link;
//    }
//
//    public int getPicture() {
//        return picture;
//    }
//
//    public void setPicture(int picture) {
//        this.picture = picture;
//    }
//}
