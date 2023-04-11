package com.github.bablo_org.bablo_project.api.model.exchangeRate;

import lombok.Value;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Value
public class CurrencyRate {
    String id;
    Double rate;
    Date updated;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        ofNullable(getId()).ifPresent(v -> map.put("id", v));
        ofNullable(getRate()).ifPresent(v -> map.put("rate", v));
        ofNullable(getUpdated()).ifPresent(v -> map.put("updated", v));
        return map;
    }
}
