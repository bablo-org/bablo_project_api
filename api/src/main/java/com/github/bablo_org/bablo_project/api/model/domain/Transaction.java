package com.github.bablo_org.bablo_project.api.model.domain;

import java.util.Date;

import com.google.cloud.firestore.DocumentSnapshot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String id;
    private String sender;
    private String receiver;
    private String currency;
    private Double amount;
    private String description;
    private Date date;
    private TransactionStatus status;
    private Date created;
    private Date updated;

    public String getPartner(String currentUser) {
        if (sender.equals(currentUser)) {
            return receiver;
        }

        if (receiver.equals(currentUser)) {
            return sender;
        }

        throw new RuntimeException("user is not related to this transaction");
    }

    public static Transaction ofDoc(DocumentSnapshot doc) {
        return new Transaction(
                doc.getId(),
                doc.getString("sender"),
                doc.getString("receiver"),
                doc.getString("currency"),
                doc.getDouble("amount"),
                doc.getString("description"),
                doc.getDate("date"),
                TransactionStatus.valueOf(doc.getString("status")),
                doc.getDate("created"),
                doc.getDate("updated")
        );
    }
}
