package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import java.util.Base64;
import java.util.List;

import com.github.bablo_org.bablo_project.api.model.User;
import com.github.bablo_org.bablo_project.api.service.UserService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(value = "/uploadAvatar")
    String uploadAvatar(@RequestBody String base64Content, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.uploadAvatar(Base64.getDecoder().decode(base64Content), userToken.getUid());
    }
}
