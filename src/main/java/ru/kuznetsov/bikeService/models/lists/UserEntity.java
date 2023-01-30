package ru.kuznetsov.bikeService.models.lists;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserEntity {
    @Column(name = "type")
    private String type;
    @Column(name = "itemId")
    private Long id;

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
}
