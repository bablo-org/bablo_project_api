package com.github.bablo_org.bablo_project.api.model.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String name;
    private String avatar;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (name != null) {
            map.put("name", name);
        }
        if (avatar != null) {
            map.put("avatar", avatar);
        }
        return map;
    }
}
