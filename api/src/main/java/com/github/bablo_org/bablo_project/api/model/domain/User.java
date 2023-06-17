package com.github.bablo_org.bablo_project.api.model.domain;

import static java.util.Optional.ofNullable;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Setter(AccessLevel.NONE)
    private String id;
    private String name;
    private String avatar;
    private boolean isActive;
    private Date created;
    @Setter(AccessLevel.NONE)
    private Date updated;
    private UserPrivateData privateData;

    public static User ofDoc(DocumentSnapshot doc) {
        return new User(
                doc.getId(),
                doc.getString("name"),
                doc.getString("avatar"),
                ofNullable(doc.getBoolean("isActive")).orElse(true),
                doc.getDate("created"),
                ofNullable(doc.getUpdateTime()).map(Timestamp::toDate).orElse(null),
                UserPrivateData.ofMap((Map<String, Object>) doc.get("privateData"))
        );
    }

    public Network simpleGetNetwork() {
        return Optional.ofNullable(privateData)
                .map(UserPrivateData::getNetwork)
                .orElse(new Network());
    }
}
