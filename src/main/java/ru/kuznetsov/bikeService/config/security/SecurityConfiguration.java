package ru.kuznetsov.bikeService.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.bikeservice.mainresources.exceptionHandlers.CustomAuthenticationFailureHandler;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.kuznetsov.bikeService.config.security.jwt.CustomAuthorizationFilter;
import ru.kuznetsov.bikeService.config.security.jwt.JwtTokenProvider;
import ru.kuznetsov.bikeService.services.CustomOAuth2UserService;
import ru.kuznetsov.bikeService.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOAuth2UserService oauthUserService;

    @Autowired
    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 JwtTokenProvider jwtTokenProvider,
                                 CustomOAuth2UserService oauthUserService) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.oauthUserService = oauthUserService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //https://www.baeldung.com/spring-security-session   - session control
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionFixation().migrateSession() // protection against typical Session Fixation attacks
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/", "/home", "/registration", "/static/**", "/login", "/oauth/**",
                            "/registrationConfirm", "/resendRegistrationToken", "/updateEmail").permitAll();
                    auth.requestMatchers("/api/auth/login/**", "/api/registration/*", "/api/pictures/**", "favicon.ico").permitAll();
                    auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/**").authenticated();
                    auth.requestMatchers("/login").anonymous();
                })
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/title")
                .failureUrl("/login?error")
                .failureHandler(authenticationFailureHandler())
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    UserModel oauthUser = new UserModel((OAuth2User) authentication.getPrincipal());
                    oauthUserService.processOAuthPostLogin(oauthUser);
                    response.sendRedirect("/successLogin");
                })
                .and()
                .addFilterBefore(new CustomAuthorizationFilter(jwtTokenProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .deleteCookies("JSESSIONID");
        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}