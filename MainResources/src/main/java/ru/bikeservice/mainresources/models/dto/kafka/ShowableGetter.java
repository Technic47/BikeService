package ru.bikeservice.mainresources.models.dto.kafka;

import java.io.Serializable;

public class ShowableGetter implements Serializable {
    private String type;
    private Long itemId;
    private Long userId;
    private boolean admin;
    private boolean shared;

    public ShowableGetter() {
    }

    public ShowableGetter(String type, Long itemId, Long userId, boolean admin, boolean shared) {
        this.type = type;
        this.itemId = itemId;
        this.userId = userId;
        this.admin = admin;
        this.shared = shared;
    }

    public ShowableGetter(String type, Long itemId) {
        this.type = type;
        this.itemId = itemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
