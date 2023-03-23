package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.ORIGIN_URL;
import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = ORIGIN_URL)
public class HelloController extends BaseController {

    @Autowired
    FirebaseAuth firebaseAuth;

    @GetMapping("/")
    String hello(@RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return String.format("Hello, %s!", userToken.getUid());
    }
}
