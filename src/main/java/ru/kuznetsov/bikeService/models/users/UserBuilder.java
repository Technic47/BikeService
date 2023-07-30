package ru.kuznetsov.bikeService.models.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserBuilder {
    private final UserModel user;

    public UserBuilder() {
        this.user = new UserModel();
    }

    public UserBuilder(String userName, String password) {
        this.user = new UserModel(userName, password);
    }

    public UserBuilder(OAuth2User oAuth2User) {
        this.user = new UserModel(oAuth2User);
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

    public UserBuilder addRole(UserRole role){
        this.user.getAuthorities().add(role);
        return this;
    }


    public UserBuilder setActive(boolean state){
        this.user.setActive(state);
        return this;
    }

    public UserBuilder setProvider(Provider provider){
        this.user.setProvider(provider);
        return this;
    }

    public UserModel build(){
        return this.user;
    }
}
