package com.github.bablo_org.bablo_project.api.service.impl;

import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.domain.User;
import com.github.bablo_org.bablo_project.api.model.domain.UserPrivateData;
import com.github.bablo_org.bablo_project.api.service.RegistrationService;
import com.github.bablo_org.bablo_project.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;

    @Override
    public void register(String id, String email) {
        User user = userService.getById(id);

        if (user == null) {
            UserPrivateData privateData = new UserPrivateData();
            privateData.setEmail(email);
            userService.addOrUpdate(id, Map.of("privateData", privateData, "isActive", true));
            return;
        }

        if (user.isActive()) {
            throw new RuntimeException("user is already exists and active");
        }
        userService.addOrUpdate(id, Map.of("isActive", true));
    }

    @Override
    public void unregister(String id) {
        User current = userService.getById(id);

        if (current == null) {
            throw new RuntimeException("user doesn't exist");
        }

        if (!current.isActive()) {
            throw new RuntimeException("user is already inactive");
        }

        userService.addOrUpdate(id, Map.of("isActive", false));
    }
}
