package com.github.bablo_org.bablo_project.api.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.github.bablo_org.bablo_project.api.model.domain.Settings;
import com.github.bablo_org.bablo_project.api.model.domain.StorageFile;
import com.github.bablo_org.bablo_project.api.model.dto.UpdateUserProfileRequest;
import com.github.bablo_org.bablo_project.api.model.domain.User;

public interface UserService {

    @Nullable
    User getById(String id);

    List<User> getAll();

    void add(Map<String, Object> fields);

    StorageFile uploadAvatar(String fileName, byte[] content, String user);

    void update(String id, Map<String, Object> fields);

    void updateCurrentProfile(UpdateUserProfileRequest profile, String userId);

    void updateSettings(Settings settings, String userId);

    void connectTelegram(String telegramUser, String userId);
}
