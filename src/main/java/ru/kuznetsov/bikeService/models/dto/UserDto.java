package ru.kuznetsov.bikeService.models.dto;

import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;

import java.util.Date;
import java.util.Set;

public class UserDto {
    protected Date created;
//    private Long id;
    private String username;
    private String email;
    private boolean active;
    private boolean enabled;
//    protected Date updated;
    private Set<UserRole> authorities;

    public UserDto(UserModel userModel) {
//        this.id = userModel.getId();
        this.username = userModel.getUsername();
        this.email = userModel.getEmail();
        this.active = userModel.isActive();
        this.enabled = userModel.isEnabled();
        this.created = userModel.getCreated();
//        this.updated = userModel.getUpdated();
        this.authorities = userModel.getAuthorities();
    }


//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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

//    public Date getUpdated() {
//        return updated;
//    }
//
//    public void setUpdated(Date updated) {
//        this.updated = updated;
//    }

    public Set<UserRole> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }
}
