package com.github.bablo_org.bablo_project.api.model.domain;

import static java.util.Optional.ofNullable;

import java.util.Date;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnershipRequest {
    private String id;
    private String sender;
    private String receiver;
    private Date created;
    private Date updated;

    public static PartnershipRequest ofDoc(DocumentSnapshot doc) {
        return new PartnershipRequest(
                doc.getId(),
                doc.getString("sender"),
                doc.getString("receiver"),
                ofNullable(doc.getCreateTime()).map(Timestamp::toDate).orElse(null),
                ofNullable(doc.getUpdateTime()).map(Timestamp::toDate).orElse(null)
        );
    }
}
