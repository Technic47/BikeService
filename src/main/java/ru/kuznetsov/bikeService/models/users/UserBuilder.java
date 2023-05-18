package ru.kuznetsov.bikeService.models.users;

import org.springframework.security.crypto.password.PasswordEncoder;

public class UserBuilder {
    private final UserModel user = new UserModel();

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

    public UserBuilder setActive(boolean state){
        this.user.setActive(state);
        return this;
    }

    public UserModel build(){
        return this.user;
    }
}
