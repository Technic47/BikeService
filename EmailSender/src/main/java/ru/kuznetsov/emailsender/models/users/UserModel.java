package ru.kuznetsov.emailsender.models.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.kuznetsov.bikeService.models.lists.UserEntity;

import java.util.*;

@Entity
@Table(name = "users")
public class UserModel implements UserDetails, OAuth2User {
    @Transient
    private final Map<String, Object> attributes;
    @Column(name = "created")
    @CreatedDate
    protected Date created;
    @Column(name = "updated")
    @LastModifiedDate
    protected Date updated;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username", unique = true)
    @NotBlank(message = "Поле не должно быть пустым!")
    @Size(min = 1, max = 255)
    private String username;
    @Column(name = "email", unique = true)
    @NotBlank(message = "Поле не должно быть пустым!")
    @Size(min = 1, max = 255)
    private String email;
    @Column(name = "active")
    private boolean active;
    @Column(name = "enabled")
    private boolean enabled;
    @ElementCollection(targetClass = UserEntity.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_item",
            joinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> createdItems = new ArrayList<>();
    @Column(name = "password", length = 1000)
    private String password;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @Column(name = "status")
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRole> authorities = new HashSet<>();


    public UserModel() {
        this.attributes = new HashMap<>();
        this.enabled = false;
    }

    public UserModel(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public UserModel(String username, String email, String password) {
        this(username, password);
        this.email = email;
    }

    public UserModel(OAuth2User oauth2User) {
        this.attributes = oauth2User.getAttributes();
        this.setUsername(this.attributes.get("email").toString());
        this.setEmail(this.attributes.get("email").toString());
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

    @Override
    public Set<UserRole> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<UserRole> authorities) {
        this.authorities = authorities;
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
        return this.attributes;
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
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getName() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel userModel)) return false;
        return active == userModel.active && Objects.equals(id, userModel.id) && Objects.equals(username, userModel.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, active, authorities, createdItems, password);
    }

    @Override
    public String toString() {
        return "Username='" + username +
                "', id=" + id +
                ", enabled=" + enabled;
    }
}
