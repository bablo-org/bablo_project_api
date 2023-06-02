package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import com.github.bablo_org.bablo_project.api.service.RegistrationService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private final RegistrationService service;

    @PostMapping("/register")
    void register(@RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.register(userToken.getUid(), userToken.getEmail());
    }

    @PostMapping("/unregister")
    void unregister(@RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.unregister(userToken.getUid());
    }
}
