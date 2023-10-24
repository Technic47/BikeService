package ru.bikeservice.mainresources.models.dto;

import java.util.HashSet;
import java.util.Set;

public class KafkaUserDto {
    private Long id;
    private boolean admin;
    private Set<UserRole> authorities = new HashSet<>();
    private String userName;

    public KafkaUserDto() {
    }

    public KafkaUserDto(Long id, boolean admin, String userName) {
        this.id = id;
        this.admin = admin;
        this.userName = userName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UserRole> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }
}