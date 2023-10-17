package ru.bikeservice.mainresources.models.dto.kafka;

import ru.bikeservice.mainresources.models.dto.UserRole;

import java.util.Set;

public class ShowableGetter {
    private String category;
    private Long id;
    private Long userId;
    private Set<UserRole> role;
    private boolean shared;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<UserRole> getRole() {
        return role;
    }

    public void setRole(Set<UserRole> role) {
        this.role = role;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
