package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.users.UserBuilder;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;
import ru.kuznetsov.bikeService.repositories.UserRepository;

import static ru.kuznetsov.bikeService.models.users.Provider.GOOGLE;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new UserModel(user);
    }

    public void processOAuthPostLogin(String username) {
        UserModel existUser = userRepository.findByUsername(username);

        if (existUser == null) {
            UserBuilder builder = new UserBuilder();
            builder.setName(username).addRole(UserRole.ROLE_USER)
                    .setProvider(GOOGLE).setActive(true);

            userRepository.save(builder.build());
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
