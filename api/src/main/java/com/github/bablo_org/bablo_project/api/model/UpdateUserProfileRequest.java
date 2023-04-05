package com.github.bablo_org.bablo_project.api.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String name;
    private String avatar;
    private String telegramId;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (name != null) {
            map.put("name", name);
        }
        if (avatar != null) {
            map.put("avatar", avatar);
        }
        if (telegramId != null) {
            map.put("telegramId", telegramId);
        }
        return map;
    }
}
