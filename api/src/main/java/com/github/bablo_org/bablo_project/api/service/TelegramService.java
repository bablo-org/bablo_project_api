package com.github.bablo_org.bablo_project.api.service;

public interface TelegramService {

    public String resolveId(String username);

    public void sendMessage(String message, String userId);
}
