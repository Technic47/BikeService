package ru.kuznetsov.bikeService.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/site")
public class SiteController {

    @GetMapping("/index")
    public String index(){
        return "site/index";
    }

    @GetMapping("/main")
    public String main(){
        return "site/Главная";
    }
}
