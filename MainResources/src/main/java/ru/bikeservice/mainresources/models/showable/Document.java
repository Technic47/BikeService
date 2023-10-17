package ru.bikeservice.mainresources.models.showable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;

@Entity
@Table(name = "documents")
public final class Document extends AbstractShowableEntity {
    public Document() {
    }

    public Document(AbstractEntityDtoNew dtoNew) {
        super(dtoNew);
    }

    public Document(EntityKafkaTransfer dtoTransfer) {
        super(dtoTransfer);
    }

    public Document(Long id, String name, String description, Long picture, String link, String value, Long creator) {
        super(id, name, description, picture, link, value, creator);
    }

    @Override
    public String getValue() {
        return this.link;
    }

    @Override
    public void setValue(String value) {
        this.link = value;
        this.value = value;
    }

    @Override
    public String getValueName() {
        return "Ссылка";
    }

    @Override
    public String getCredentials() {
        return name;
    }
}