package com.github.bablo_org.bablo_project.api.model.domain;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
    private List<String> favoriteCurrencies;
    private Boolean enableTelegramNotifications;

    public static Settings empty() {
        return new Settings(emptyList(), false);
    }

    /** setting is a field of 'user' document */
    public static Settings ofMap(Map<String, Object> map) {
        return map == null
               ? empty()
               : new Settings(
                       (List<String>) map.get("favoriteCurrencies"),
                       (Boolean) map.get("enableTelegramNotifications")
               );
    }
}
