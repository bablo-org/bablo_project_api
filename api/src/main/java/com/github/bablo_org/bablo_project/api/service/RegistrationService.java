package com.github.bablo_org.bablo_project.api.service;

public interface RegistrationService {

    void register(String id, String email);

    void unregister(String id);
}
