package com.github.bablo_org.bablo_project.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

    @Value("${NAME:World}")
    String name;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
