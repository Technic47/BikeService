package ru.kuznetsov.bikeService.models.dto;

import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;

import java.util.Date;
import java.util.Set;

public class UserDto {
    protected Date created;
    private String username;
    private String email;
    private boolean active;
    private boolean enabled;
    private Set<UserRole> authorities;

    public UserDto(UserModel userModel) {
        this.username = userModel.getUsername();
        this.email = userModel.getEmail();
        this.active = userModel.isActive();
        this.enabled = userModel.isEnabled();
        this.created = userModel.getCreated();
        this.authorities = userModel.getAuthorities();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Set<UserRole> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }
}
