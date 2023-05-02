package com.github.bablo_org.bablo_project.api.model.exchangeRate;

import lombok.Value;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;


@Value
public class CurrencyInfo {
    String id;
    String name;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        ofNullable(getId()).ifPresent(v -> map.put("id", v));
        ofNullable(getName()).ifPresent(v -> map.put("name", v));
        return map;
    }

}
