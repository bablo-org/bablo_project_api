package com.github.bablo_org.bablo_project.api.service;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.StorageFile;
import com.github.bablo_org.bablo_project.api.model.User;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String DB_COLLECTION_NAME = "users";

    private static final String STORAGE_BUCKET_NAME = "artifacts.bablo-project.appspot.com";

    private final Firestore firestore;

    private final Storage cloudStorage;

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
                        doc.getString("avatar"),
                        doc.getDate("created")
                ))
                .collect(toList());
    }

    @SneakyThrows
    public User updateCurrentProfile(User update, String callerId) {
        DocumentReference ref = getRefById(callerId);
        DocumentSnapshot doc = ref.get().get();
        if (!doc.exists()) {
            throw new RuntimeException("User with such id does note exist");
        }

        User user = toModel(doc);
        validateUpdateProfile(user, callerId);
        if (update.getName() != null) {
            user.setName(update.getName());
        }
        if (update.getAvatar() != null) {
            user.setAvatar(update.getAvatar());
        }

        Map<String, Object> fields = new HashMap<>();
        ofNullable(update.getAvatar()).ifPresent(v -> fields.put("avatar", v));
        ofNullable(update.getName()).ifPresent(v -> fields.put("name", update.getName()));

        if (!fields.isEmpty()) {
            ref.update(fields).get();
        }

        return user;
    }

    public StorageFile uploadAvatar(byte[] content, String user) {
        String fileName = "avatars/avatar-" + user; // "avatars" is a folder inside a bucket
        BlobInfo info = BlobInfo.newBuilder(STORAGE_BUCKET_NAME, fileName)
                .build();
        try (InputStream is = new ByteArrayInputStream(content)) {
            Blob blob = cloudStorage.createFrom(info, is);
            return new StorageFile(blob.getMediaLink());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private DocumentReference getRefById(String id) {
        return firestore
                .collection(DB_COLLECTION_NAME)
                .document(id);
    }

    private User toModel(DocumentSnapshot doc) {
        return new User(
                doc.getId(),
                doc.getString("name"),
                doc.getString("email"),
                doc.getString("avatar"),
                doc.getDate("created")
        );
    }

    private void validateUpdateProfile(User user, String callerId) {
        if (user == null) {
            throw new RuntimeException("can't update non-existent user");
        }

        if (!user.getId().equals(callerId)) {
            throw new RuntimeException("can't update user - current user is not a user to be updated");
        }
    }
}
