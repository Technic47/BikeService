package ru.kuznetsov.bikeService.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.kuznetsov.bikeService.services.CustomUserDetailsService;


@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfiguration(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .addFilter()
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
        ;
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails pavel = User.builder()
                .username("pavel")
                .password(passwordEncoder().encode("1999"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(pavel);
    }
}