package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.users.CustomOAuth2User;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.repositories.UserRepository;

import static ru.kuznetsov.bikeService.models.users.Provider.GOOGLE;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new CustomOAuth2User(user);
    }

    public void processOAuthPostLogin(String username) {
        UserModel existUser = userRepository.findByUsername(username);

        if (existUser == null) {
            UserModel newUser = new UserModel();
            newUser.setUsername(username);
            newUser.setProvider(GOOGLE);
            newUser.setActive(true);

            userRepository.save(newUser);
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
