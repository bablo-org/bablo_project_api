package com.github.bablo_org.bablo_project.api.service;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Settings;
import com.github.bablo_org.bablo_project.api.model.StorageFile;
import com.github.bablo_org.bablo_project.api.model.UpdateUserProfileRequest;
import com.github.bablo_org.bablo_project.api.model.User;

public interface UserService {

    List<User> getAll();

    User getById(String id);

    void updateCurrentProfile(UpdateUserProfileRequest profile, String userId);

    void updateSettings(Settings settings, String userId);

    StorageFile uploadAvatar(String fileName, byte[] content, String user);

    void connectTelegram(String telegramUser, String userId);
}
