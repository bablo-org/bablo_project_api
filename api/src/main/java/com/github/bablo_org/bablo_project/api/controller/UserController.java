package com.github.bablo_org.bablo_project.api.controller;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.User;
import com.github.bablo_org.bablo_project.api.service.UserService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends BaseController {

    private final UserService service;

    @GetMapping
    @ResponseBody
    List<User> getAll() {
        return service.getAll();
    }

    @PutMapping("/updateProfile")
    User updateProfile(@RequestBody User user, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.updateCurrentProfile(user, userToken.getUid());
    }
}
