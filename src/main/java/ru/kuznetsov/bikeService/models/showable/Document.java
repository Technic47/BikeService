package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

@Entity
@Table(name = "documents")
public class Document extends AbstractShowableEntity implements Showable {

    //    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    protected Long id;
//    @NotEmpty(message = "Fill this field!")
//    @Column(name = "name")
//    protected String name;
//    @Column(name = "description")
//    protected String description;
    @Column(name = "link")
    protected String link;
//    @Column(name = "picture")
//    protected Long picture;
    @Transient
    protected String value;

    public Document() {
    }

//    public Document() {
//        this.name = "";
//        this.description = "";
//        this.link = "";
//        this.picture = 1;
//    }


//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    @Override
    public String getValue() {
        return this.link;
    }

    public void setValue(String value) {
        this.value = value;
    }


//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

//    public Long getPicture() {
//        return picture;
//    }
//
//    public void setPicture(Long picture) {
//        this.picture = picture;
//    }
}
