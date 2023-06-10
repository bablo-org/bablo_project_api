package com.github.bablo_org.bablo_project.api.model.domain;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.bablo_org.bablo_project.api.utils.FirestoreUtils;
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
    private Set<Partner> partners;
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
                extractPartners(doc),
                ofNullable(doc.getBoolean("isActive"))
                        .orElse(true)
        );
    }

    private static Set<Partner> extractPartners(DocumentSnapshot doc) {
        List<Map<String, Object>> partners = (List<Map<String, Object>>) doc.get("partners");

        if (partners == null) {
            return Collections.emptySet();
        }

        return partners
                .stream()
                .map(fields -> new Partner(
                        (String) fields.get("id"),
                        FirestoreUtils.listToSet(fields.get("tags"))
                ))
                .collect(toSet());
    }
}
