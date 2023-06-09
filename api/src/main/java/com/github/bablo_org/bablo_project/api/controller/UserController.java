package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import java.util.Base64;
import java.util.Collections;
import java.util.List;

import com.github.bablo_org.bablo_project.api.model.domain.Settings;
import com.github.bablo_org.bablo_project.api.model.domain.StorageFile;
import com.github.bablo_org.bablo_project.api.model.domain.User;
import com.github.bablo_org.bablo_project.api.model.dto.UpdatePartnerTagsRequest;
import com.github.bablo_org.bablo_project.api.model.dto.UpdateUserProfileRequest;
import com.github.bablo_org.bablo_project.api.service.UserService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends BaseController {

    private final UserService service;

    public enum Filter {
        partners,
        all
    }

    @GetMapping
    @ResponseBody
    List<User> getAll(@RequestParam(name = "filter", defaultValue = "all") Filter scope,
                      @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        List<User> users = scope == Filter.partners
                           ? service.getUserWithPartners(userToken.getUid())
                           : service.getAll();
        return hidePrivateData(users, userToken.getUid());
    }

    @GetMapping("/{id}")
    @ResponseBody
    List<User> getById(@PathVariable("id") String id, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        User user = service.getById(id);
        if (user == null) {
            return Collections.emptyList();
        } else {
            List<User> result = Collections.singletonList(user);
            return hidePrivateData(result, userToken.getUid());
        }
    }

    @PutMapping("/connectTelegram/{telegramUser}")
    ResponseEntity<String> connectTelegram(@PathVariable("telegramUser") String telegramUser,
                                           @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.connectTelegram(telegramUser, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/updateProfile")
    ResponseEntity<String> updateProfile(@RequestBody UpdateUserProfileRequest request,
                                         @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.updateCurrentProfile(request, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/updateSettings")
    ResponseEntity<String> updateSettings(@RequestBody Settings settings, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.updateSettings(settings, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/updatePartnerTags")
    ResponseEntity<String> updateSettings(@RequestBody UpdatePartnerTagsRequest request,
                                          @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.updatePartnerTags(userToken.getUid(), request.getPartnerId(), request.getTags());
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping(value = "/uploadAvatar")
    StorageFile uploadAvatar(@RequestParam("fileName") String fileName,
                             @RequestBody String base64Content,
                             @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.uploadAvatar(fileName, Base64.getDecoder().decode(base64Content), userToken.getUid());
    }

    List<User> hidePrivateData(List<User> users, String currentUserId) {
        for (User user : users) {
            if (!user.getId().equals(currentUserId)) {
                user.setPrivateData(null);
            }
        }
        return users;
    }
}
