package ru.kuznetsov.bikeService.models.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kuznetsov.bikeService.models.lists.UserEntity;

import java.util.List;
import java.util.Set;

public class UserBuilder {
    private final UserModel user = new UserModel();

    public UserBuilder() {
    }

    public UserBuilder(UserModel user) {
        this.setName(user.getUsername());
        this.setPassword(user.getPassword());
    }

    public UserBuilder setName(String name){
        this.user.setUsername(name);
        return this;
    }

    public UserBuilder setPassword(String password){
        this.user.setPassword(password);
        return this;
    }

    public UserBuilder encodePassword(PasswordEncoder encoder){
        this.user.setPassword(encoder.encode(this.user.getPassword()));
        return this;
    }

    public UserBuilder setRole(UserRole role){
        this.user.getStatus().add(role);
        return this;
    }

    public UserBuilder setRole(Set<UserRole> roles){
        this.user.setStatus(roles);
        return this;
    }

    public UserBuilder setActive(boolean state){
        this.user.setActive(state);
        return this;
    }

    public UserBuilder setCreatedItems(List<UserEntity> newList){
        this.user.setCreatedItems(newList);
        return this;
    }

    public UserModel build(){
        return this.user;
    }
}
