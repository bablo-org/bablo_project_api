package com.github.bablo_org.bablo_project.api.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.User;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Firestore firestore;

    @SneakyThrows
    public List<User> getAll() {
        return firestore.collection("users")
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> new User(
                        doc.getId(),
                        doc.getString("name"),
                        doc.getString("email"),
                        doc.getDate("created")
                ))
                .collect(toList());
    }
}
