package com.github.bablo_org.bablo_project.api.model;

import static java.util.Optional.ofNullable;

import java.util.Date;

import com.google.cloud.firestore.DocumentSnapshot;
import lombok.Value;

@Value
public class Currency {
    String id;
    String name;
    String symbol;
    boolean isActive;
    Double rate;
    Date updated;

    public static Currency ofDoc(DocumentSnapshot doc) {
        return new Currency(
                doc.getId(),
                doc.getString("name"),
                doc.getString("symbol"),
                ofNullable(doc.getBoolean("isActive")).orElse(true),
                doc.getDouble("rate"),
                doc.getDate("updated")
        );
    }
}
