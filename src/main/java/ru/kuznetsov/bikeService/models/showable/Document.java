package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

@Entity
@Table(name = "documents")
public class Document extends AbstractShowableEntity implements Showable {
    public Document() {
    }

    @Override
    public String getValue() {
        return this.link;
    }

    @Override
    public String getValueName() {
        return "Ссылка";
    }

    @Override
    public void setValue(String value) {
        this.link = value;
    }
}