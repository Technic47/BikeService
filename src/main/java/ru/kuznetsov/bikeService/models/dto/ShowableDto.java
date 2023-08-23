package ru.kuznetsov.bikeService.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.kuznetsov.bikeService.models.showable.Showable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShowableDto {
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
    @Size(max = 1000)
    @Column(name = "link")
    protected String link;

    @NotBlank(message = "Поле не должно быть пустым!")
    @Size(min = 1, max = 200)
    @Column(name = "value")
    protected String value;

    public ShowableDto(Showable toConvert) {
        this.id = toConvert.getId();
        this.name = toConvert.getName();
        this.description = toConvert.getDescription();
        this.picture = toConvert.getPicture();
        this.link = toConvert.getLink();
        this.value = toConvert.getValue();
    }
}
