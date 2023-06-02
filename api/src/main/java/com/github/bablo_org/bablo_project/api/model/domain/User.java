package com.github.bablo_org.bablo_project.api.model.domain;

import static java.util.Optional.ofNullable;

import java.util.Date;

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
    private String email;
    private String avatar;
    private String telegramUser;
    private String telegramId;
    @Setter(AccessLevel.NONE)
    private Date created;
    @Setter(AccessLevel.NONE)
    private Date updated;
    private Settings settings;
    private boolean isActive;

    public static User ofDoc(DocumentSnapshot doc) {
        return new User(
                doc.getId(),
                doc.getString("name"),
                doc.getString("email"),
                doc.getString("avatar"),
                doc.getString("telegramUser"),
                doc.getString("telegramId"),
                doc.getDate("created"),
                ofNullable(doc.getUpdateTime())
                        .map(Timestamp::toDate)
                        .orElse(null),
                Settings.ofDoc(doc),
                ofNullable(doc.getBoolean("isActive"))
                        .orElse(true)
        );
    }
}
