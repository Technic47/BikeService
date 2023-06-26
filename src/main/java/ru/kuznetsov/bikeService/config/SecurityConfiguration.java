package ru.kuznetsov.bikeService.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.users.CustomOAuth2User;
import ru.kuznetsov.bikeService.services.CustomOAuth2UserService;
import ru.kuznetsov.bikeService.services.CustomUserDetailsService;

import java.io.IOException;

@Component
@EnableWebSecurity
public class SecurityConfiguration {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private CustomOAuth2UserService oauthUserService;

    @Autowired
    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/", "/home", "/registration", "/static/**", "/oauth/**").permitAll();
                    auth.requestMatchers("/**").authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {

                        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

                        oauthUserService.processOAuthPostLogin(oauthUser.getEmail());

                        response.sendRedirect("/list");
                    }
                });
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}