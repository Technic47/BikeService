package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SiteController {

    @GetMapping("/home")
    public String home(){
        return "title";
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "home";
    }
}
