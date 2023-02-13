package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

@Entity
@Table(name = "documents")
public class Document extends AbstractShowableEntity implements Showable {

//    @ManyToMany(mappedBy = "linked_documents",
//            cascade = CascadeType.ALL)
//    List<UserModel> users = new ArrayList<>();
//
//    public List<UserModel> getUsers() {
//        return users;
//    }
//
//    public void setUsers(List<UserModel> parts) {
//        this.users = parts;
//    }

    public Document() {
    }

    @Override
    public String getValue() {
        return this.link;
    }

    @Override
    public String getValueName() {
        return "Link";
    }

    @Override
    public void setValue(String value) {
        this.link = value;
    }
}