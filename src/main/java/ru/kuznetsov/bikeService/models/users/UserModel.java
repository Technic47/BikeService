package ru.kuznetsov.bikeService.models.users;

import com.google.gson.Gson;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
public class UserModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //    private String email;
//    private String phone;
    @Column(name = "name", unique = true)
    private String username;
    @Column(name = "active")
    private boolean active;
    //    private int avatar;
    @Column(name = "status")
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> status = new HashSet<>();
//    private Map<String, List<Long>> createdItems;
    @Column(name = "password", length = 1000)
    private String password;
    @Transient
    private Gson converter;
    @Column(name = "createdItems")
    private String createdItems;

    public UserModel() {
        this.converter = new Gson();
        this.createdItems = this.converter.toJson(new HashMap<String, List<Long>>());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<UserRole> getStatus() {
        return status;
    }

    public void setStatus(Set<UserRole> status) {
        this.status = status;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return status;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return active == userModel.active && id.equals(userModel.id) && username.equals(userModel.username) && status.equals(userModel.status) && password.equals(userModel.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, active, status, password);
    }
}
