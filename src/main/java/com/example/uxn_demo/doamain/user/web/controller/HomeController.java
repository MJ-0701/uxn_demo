package com.example.uxn_demo.doamain.user.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class HomeController {

    @GetMapping("")
    public String greeting(){
        return "hello";
    }
}
