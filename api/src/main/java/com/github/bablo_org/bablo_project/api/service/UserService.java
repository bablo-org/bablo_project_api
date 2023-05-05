package com.github.bablo_org.bablo_project.api.service;

import com.github.bablo_org.bablo_project.api.model.Settings;
import com.github.bablo_org.bablo_project.api.model.StorageFile;
import com.github.bablo_org.bablo_project.api.model.UpdateUserProfileRequest;
import com.github.bablo_org.bablo_project.api.model.User;

import java.util.List;

public interface UserService {

    public List<User> getAll();

    public User getById(String id);

    public void updateCurrentProfile(UpdateUserProfileRequest profile, String userId);

    public void updateSettings(Settings settings, String userId);

    public StorageFile uploadAvatar(String fileName, byte[] content, String user);

    public void connectTelegram(String telegramUser, String userId);
}
