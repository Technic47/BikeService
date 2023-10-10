package ru.kuznetsov.bikeService.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDtoNew;

@Entity
@Table(name = "documents")
public final class Document extends AbstractShowableEntity {
    public Document() {
    }

    public Document(AbstractEntityDtoNew dtoNew) {
        super(dtoNew);
    }

    public Document(Long id, String name, String description, Long picture, String link, String value, Long creator) {
        super(id, name, description, picture, link, value, creator);
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
        this.value = value;
    }

    @Override
    public String getCredentials() {
        return name;
    }
}