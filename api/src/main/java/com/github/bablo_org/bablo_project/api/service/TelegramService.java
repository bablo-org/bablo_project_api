package com.github.bablo_org.bablo_project.api.service;

public interface TelegramService {

    String resolveId(String username);

    void sendMessage(String message, String userId);
}
