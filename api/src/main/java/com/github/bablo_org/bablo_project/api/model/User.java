package com.github.bablo_org.bablo_project.api.model;

import java.util.Date;

import lombok.Value;

@Value
public class User {
    String id;
    String name;
    String email;
    Date created;
}
