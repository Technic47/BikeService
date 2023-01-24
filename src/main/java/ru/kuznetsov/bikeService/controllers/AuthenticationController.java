//package ru.kuznetsov.bikeService.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.kuznetsov.bikeService.DAO.UserDAO;
//import ru.kuznetsov.bikeService.config.security.JwtUtils;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthenticationController {
//    private AuthenticationManager authenticationManager;
//    private UserDAO userDAO;
//    private JwtUtils jwtUtils;
//
////    public AuthenticationController(AuthenticationManager authenticationManager, UserDAO userDAO, JwtUtils jwtUtils) {
////        this.authenticationManager = authenticationManager;
////        this.userDAO = userDAO;
////        this.jwtUtils = jwtUtils;
////    }
//
//
//    @PostMapping("/authenticate")
//    public ResponseEntity<String> authentication(
//            @RequestBody AuthenticationRequest request
//    ) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//        final UserDetails user = userDAO.findUserByEmail(request.getEmail());
//        if (user != null) {
//            return ResponseEntity.ok(jwtUtils.generateToken(user));
//        }
//        return ResponseEntity.status(400).body("Some error has occurred!");
//    }
//
//    @Autowired
//    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Autowired
//    public void setUserDAO(UserDAO userDAO) {
//        this.userDAO = userDAO;
//    }
//
//    @Autowired
//    public void setJwtUtils(JwtUtils jwtUtils) {
//        this.jwtUtils = jwtUtils;
//    }
//}
