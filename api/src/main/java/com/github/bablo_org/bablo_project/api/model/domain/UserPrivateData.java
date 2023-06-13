package com.github.bablo_org.bablo_project.api.model.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivateData {
    private String telegramUser;
    private String telegramId;
    private Settings settings;

    public static UserPrivateData ofMap(Map<String, Object> map) {
        return new UserPrivateData(
                (String) map.get("telegramUser"),
                (String) map.get("telegramId"),
                Settings.ofMap((Map<String, Object>) map.get("settings"))
        );
    }
}
