package com.homework.bank.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HomeController {

    @GetMapping("/")
    public String info() {
        return "Test that back-end works";
    }
}
