package ru.kuznetsov.bikeService.models.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.kuznetsov.bikeService.models.lists.UserEntity;

import java.util.*;

@Entity
@Table(name = "users")
public class UserModel implements UserDetails, OAuth2User {
    @Transient
    private OAuth2User oauth2User;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username", unique = true)
    @NotBlank(message = "Поле не должно быть пустым!")
    @Size(min = 1, max = 255)
    private String username;
    @Column(name = "active")
    private boolean active;

    @Column(name = "status")
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> status = new HashSet<>();

    @ElementCollection(targetClass = UserEntity.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_item",
            joinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> createdItems = new ArrayList<>();

    @Column(name = "password", length = 1000)
    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;


    public UserModel() {
    }

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserModel(OAuth2User oauth2User) {
        this.oauth2User = oauth2User;
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

    public List<UserEntity> getCreatedItems() {
        return createdItems;
    }

    public void setCreatedItems(List<UserEntity> createdItems) {
        this.createdItems = createdItems;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.oauth2User.getAttributes();
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
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;
        UserModel userModel = (UserModel) o;
        return active == userModel.active && Objects.equals(id, userModel.id) && Objects.equals(username, userModel.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, active, status, createdItems, password);
    }

    @Override
    public String toString() {
        return "Username='" + username +
                "', id=" + id +
                ", active=" + active;
    }
}
