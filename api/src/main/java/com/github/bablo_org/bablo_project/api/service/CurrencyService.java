package com.github.bablo_org.bablo_project.api.service;

import static com.google.cloud.firestore.FieldPath.documentId;
import static com.google.cloud.firestore.Filter.equalTo;
import static com.google.cloud.firestore.Filter.inArray;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.model.User;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final String COLLECTION_NAME = "currencies";

    private final Firestore firestore;

    private final UserService userService;

    @SneakyThrows
    public List<Currency> getAll() {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(this::toModel)
                .collect(toList());
    }

    @SneakyThrows
    public List<Currency> getByStatus(boolean isActive) {
        return firestore.collection(COLLECTION_NAME)
                .where(equalTo("isActive", isActive))
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(this::toModel)
                .collect(toList());
    }

    @SneakyThrows
    public void changeStatus(List<String> ids, boolean isActive, String userId) {
        User user = userService.getById(userId);
        if (!user.isAdmin()) {
            throw new RuntimeException("only admin users may activate/deactivate currencies");
        }

        WriteBatch batch = firestore.batch();

        firestore.collection(COLLECTION_NAME)
                .where(inArray(documentId(), ids))
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(DocumentSnapshot::getReference)
                .forEach(ref -> batch.update(ref, Map.of("isActive", isActive)));

        batch.commit().get();
    }

    private Currency toModel(DocumentSnapshot doc) {
        return new Currency(
                doc.getId(),
                doc.getString("name"),
                doc.getString("symbol"),
                ofNullable(doc.getBoolean("isActive")).orElse(true),
                doc.getDouble("rate"),
                doc.getDate("updated")
        );
    }
}
