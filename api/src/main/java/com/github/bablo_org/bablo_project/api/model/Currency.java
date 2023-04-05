package com.github.bablo_org.bablo_project.api.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.DocumentSnapshot;
import lombok.Value;

import static java.util.Optional.ofNullable;

@Value
public class Currency {
    String id;
    String name;
    String symbol;
    Double rate;
    Date updated;

    public static Currency ofDoc(DocumentSnapshot doc) {
        return new Currency(
                doc.getId(),
                doc.getString("name"),
                doc.getString("symbol"),
                doc.getDouble("rate"),
                doc.getDate("updated")
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        ofNullable(getId()).ifPresent(v -> map.put("id", v));
        ofNullable(getName()).ifPresent(v -> map.put("name", v));
        ofNullable(getSymbol()).ifPresent(v -> map.put("symbol", v));
        ofNullable(getRate()).ifPresent(v -> map.put("rate", v));
        ofNullable(getUpdated()).ifPresent(v -> map.put("updated", v));

        return map;
    }

}
