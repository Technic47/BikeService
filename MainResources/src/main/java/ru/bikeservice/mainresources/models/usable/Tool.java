package ru.bikeservice.mainresources.models.usable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import ru.bikeservice.mainresources.models.abstracts.AbstractUsableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;


@Entity
@Table(name = "tools")
public final class Tool extends AbstractUsableEntity {
    public Tool() {
    }

    public Tool(Long id, String name, String description, Long picture, String link, String value, Long creator, Long manufacturer, String model) {
        super(id, name, description, picture, link, value, creator, manufacturer, model);
    }

    public Tool(AbstractEntityDtoNew dtoNew) {
        super(dtoNew);
    }

    @Override
    public String getValueName() {
        return "Размерность";
    }
}
