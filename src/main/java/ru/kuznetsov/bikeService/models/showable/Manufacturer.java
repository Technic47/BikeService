package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

@Entity
@Table(name = "manufacturers")
public class Manufacturer extends AbstractShowableEntity implements Showable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    protected Long id;
//    @NotEmpty(message = "Fill this field!")
//    @Column(name = "name")
//    protected String name;
//    @Column(name = "description")
//    protected String description;
//    //    @Column(name="link")
////    protected String link;
//    @Column(name = "picture")
//    protected Long picture;
    @Transient
    protected String value;
    @Column(name = "country")
    private String country;

//    @Override
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }


//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getName() {
//        return name;
//    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }
    @Override
    public String getValue() {
        return this.country;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

//    @Override
//    public String getDescription() {
//        return this.description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//
//
//    public Long getPicture() {
//        return picture;
//    }
//
//    public void setPicture(Long picture) {
//        this.picture = picture;
//    }

    @Override
    public String toString() {
        return "Manufacturer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
