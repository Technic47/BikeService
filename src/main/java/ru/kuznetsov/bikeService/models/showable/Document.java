package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

@Entity
@Table(name = "documents")
public class Document extends AbstractShowableEntity implements Showable {
    @Column(name = "link")
    protected String link;
    @Transient
    protected String value;

    public Document() {
    }


    @Override
    public String getValue() {
        return this.link;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
