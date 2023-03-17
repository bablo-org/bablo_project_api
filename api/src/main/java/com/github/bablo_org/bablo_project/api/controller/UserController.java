package com.github.bablo_org.bablo_project.api.controller;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.User;
import com.github.bablo_org.bablo_project.api.repository.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService service;

    @GetMapping
    List<User> getAll() {
        return service.getAll();
    }
}
