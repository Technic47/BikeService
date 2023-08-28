package ru.kuznetsov.bikeService.models.lists;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

import java.util.Objects;

@Embeddable
public class UserEntity {
    @Column(name = "type")
    private String type;
    @Column(name = "itemId")
    private Long id;

    public UserEntity(AbstractShowableEntity item) {
        this.type = item.getClass().getSimpleName();
        this.id = item.getId();
    }

    public UserEntity(String type, Long id) {
        this.type = type;
        this.id = id;
    }

    public UserEntity() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return type.equals(that.type) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }
}
