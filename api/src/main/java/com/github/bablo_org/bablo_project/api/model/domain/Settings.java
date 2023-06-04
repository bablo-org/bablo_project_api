package com.github.bablo_org.bablo_project.api.model.domain;

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
    private Boolean enableTelegramNotifications;

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
}
