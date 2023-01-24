//package ru.kuznetsov.bikeService.DAO;
//
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Repository;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//@Repository
//public class UserDAO {
//    private final static List<UserDetails> APLICATION_USERS = Arrays.asList(
//            new User(
//                    "email@mail.com",
//                    "password",
//                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
//            ),
//            new User(
//                    "newemail@.gmail.com",
//                    "password",
//                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
//            )
//    );
//
//    public UserDetails findUserByEmail(String email) {
//        return APLICATION_USERS
//                .stream()
//                .filter(u -> u.getUsername().equals(email))
//                .findFirst()
//                .orElseThrow(() -> new UsernameNotFoundException("No user found!"))
//                ;
//    }
//}
