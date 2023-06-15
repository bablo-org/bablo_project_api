package com.github.bablo_org.bablo_project.api.model.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivateData {
    private String email;
    private String telegramUser;
    private String telegramId;
    private Settings settings;

    public static UserPrivateData empty() {
        return new UserPrivateData("", "", "", Settings.empty());
    }

    public static UserPrivateData ofMap(Map<String, Object> map) {
        return map == null
               ? empty()
               : new UserPrivateData(
                       (String) map.get("email"),
                       (String) map.get("telegramUser"),
                       (String) map.get("telegramId"),
                       Settings.ofMap((Map<String, Object>) map.get("settings"))
               );
    }
}
