package com.github.bablo_org.bablo_project.api.model;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String name;
    private String avatar;
}
