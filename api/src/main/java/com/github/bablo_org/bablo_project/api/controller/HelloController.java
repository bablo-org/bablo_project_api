package com.github.bablo_org.bablo_project.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    String hello() {
        return "Hello!";
    }
}
