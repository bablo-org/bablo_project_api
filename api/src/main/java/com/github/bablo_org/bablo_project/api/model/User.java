package com.github.bablo_org.bablo_project.api.model;

import static java.util.Optional.ofNullable;

import java.util.Date;

import com.google.cloud.firestore.DocumentSnapshot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String email;
    private String avatar;
    private Date created;
    private boolean isAdmin;
    private Settings settings;

    public static User ofDoc(DocumentSnapshot doc) {
        return new User(
                doc.getId(),
                doc.getString("name"),
                doc.getString("email"),
                doc.getString("avatar"),
                doc.getDate("created"),
                ofNullable(doc.getBoolean("isAdmin")).orElse(false),
                Settings.ofDoc(doc)
        );
    }
}
