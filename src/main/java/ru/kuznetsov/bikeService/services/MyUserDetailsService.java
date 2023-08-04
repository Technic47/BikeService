//package ru.kuznetsov.bikeService.services;
//
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import ru.kuznetsov.bikeService.models.users.UserModel;
//import ru.kuznetsov.bikeService.repositories.UserRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@Transactional
//public class MyUserDetailsService implements UserDetailsService {
//    private final UserRepository userRepository;
//
//    @Autowired
//    public MyUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        boolean enabled = true;
//        boolean accountNonExpired = true;
//        boolean credentialsNonExpired = true;
//        boolean accountNonLocked = true;
//        try {
//            UserModel user = userRepository.findByEmail(email);
//            if (user == null) {
//                throw new UsernameNotFoundException(
//                        "No user found with username: " + email);
//            }
//
//            return new org.springframework.security.core.userdetails.User(
//                    user.getEmail(),
//                    user.getPassword().toLowerCase(),
//                    user.isEnabled(),
//                    accountNonExpired,
//                    credentialsNonExpired,
//                    accountNonLocked,
//                    user.getAuthorities());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
