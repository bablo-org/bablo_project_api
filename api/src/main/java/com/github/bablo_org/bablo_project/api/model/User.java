package com.github.bablo_org.bablo_project.api.model;

import java.time.LocalDate;

import lombok.Value;

@Value
public class User {
    String id;
    String name;
    String email;
    byte[] avatar;
    LocalDate created;
}
