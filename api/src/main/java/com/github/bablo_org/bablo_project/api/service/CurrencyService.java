package com.github.bablo_org.bablo_project.api.service;

import static com.google.cloud.firestore.Filter.equalTo;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private static final String COLLECTION_NAME = "currencies";

    private final Firestore firestore;

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
