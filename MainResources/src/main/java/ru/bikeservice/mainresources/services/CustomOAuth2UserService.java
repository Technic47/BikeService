package ru.bikeservice.mainresources.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.bikeservice.mainresources.models.users.UserBuilder;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.bikeservice.mainresources.models.users.UserRole;
import ru.bikeservice.mainresources.repositories.UserRepository;

import static ru.bikeservice.mainresources.models.users.Provider.GOOGLE;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return new UserModel(user);
    }

//    @Transactional(isolation = Isolation.READ_COMMITTED,
//            rollbackFor = {SQLException.class, RuntimeException.class})
    public void processOAuthPostLogin(UserModel oAuth2User) {
        String oAuth2UserEmail = oAuth2User.getEmail();
        UserModel existUser = userRepository.findByEmail(oAuth2UserEmail);

        if (existUser == null) {
            UserBuilder builder = new UserBuilder(oAuth2User);
            builder.setName(oAuth2UserEmail).setEmail(oAuth2UserEmail).addRole(UserRole.ROLE_USER)
                    .setProvider(GOOGLE).setActive(true).setEnabled(true);

            userRepository.save(builder.build());
        }
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
