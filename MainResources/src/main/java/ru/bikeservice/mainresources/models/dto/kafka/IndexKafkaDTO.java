package ru.bikeservice.mainresources.models.dto.kafka;

import java.io.Serializable;

public class IndexKafkaDTO implements Serializable {
    private String type;
    private Long userId;
    private boolean admin;
    private boolean shared;

    public IndexKafkaDTO() {
    }

    public IndexKafkaDTO(String type, Long userId, boolean admin, boolean shared) {
        this.type = type;
        this.userId = userId;
        this.admin = admin;
        this.shared = shared;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
