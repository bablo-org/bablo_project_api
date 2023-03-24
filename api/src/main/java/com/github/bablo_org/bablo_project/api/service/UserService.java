package com.github.bablo_org.bablo_project.api.service;

import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.Transaction;
import com.github.bablo_org.bablo_project.api.model.TransactionStatus;
import com.github.bablo_org.bablo_project.api.model.User;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String COLLECTION_NAME = "users";
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

    @SneakyThrows
    public User updateCurrentProfile(User user, String callerId) {
        DocumentReference ref = getRefById(callerId);
        DocumentSnapshot doc = ref.get().get();
        if (!doc.exists()) {
            new RuntimeException("User with such id does note exist");
        }

        User recorderUser = toModel(doc);
        validateUpdateProfile(recorderUser, callerId);
        recorderUser.setName(user.getName());

        ref.update(Map.of(
                "name", user.getName()
        )).get();

        return recorderUser;
    }

    @SneakyThrows
    private DocumentReference getRefById(String id) {
        return firestore
                .collection(COLLECTION_NAME)
                .document(id);
    }

    private User toModel(DocumentSnapshot doc) {
        return new User(
                doc.getId(),
                doc.getString("name"),
                doc.getString("email"),
                doc.getDate("created")
        );
    }

    private void validateUpdateProfile (User user, String callerId) {
        if (user == null) {
            throw new RuntimeException("can't update non-existent user");
        }

        if (!user.getId().equals(callerId)) {
            throw new RuntimeException("can't update user - current user is not a user to be updated");
        }
    }
}
