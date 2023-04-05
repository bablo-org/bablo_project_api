package com.github.bablo_org.bablo_project.api.model;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentSnapshot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
    private List<String> favoriteCurrencies;
    private boolean enableTelegramNotifications;

    /** setting is a field of 'user' document */
    public static Settings ofDoc(DocumentSnapshot doc) {
        Map<String, Object> settings = (Map<String, Object>) doc.get("settings");

        if (settings == null) {
            return new Settings(emptyList(), false);
        }

        return new Settings(
                (List<String>) settings.get("favoriteCurrencies"),
                (Boolean) settings.get("enableTelegramNotifications")
        );
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "favoriteCurrencies", favoriteCurrencies,
                "enableTelegramNotifications", enableTelegramNotifications
        );
    }
}
