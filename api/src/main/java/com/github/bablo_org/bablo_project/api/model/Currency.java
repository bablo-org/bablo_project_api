package com.github.bablo_org.bablo_project.api.model;

import java.util.Date;

import com.google.cloud.firestore.DocumentSnapshot;
import lombok.Value;

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
}
