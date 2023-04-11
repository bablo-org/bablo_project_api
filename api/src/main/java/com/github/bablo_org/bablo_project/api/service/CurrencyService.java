package com.github.bablo_org.bablo_project.api.service;

import static com.google.cloud.firestore.FieldPath.documentId;
import static com.google.cloud.firestore.Filter.inArray;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.model.Transaction;
import com.github.bablo_org.bablo_project.api.model.currencyExternal.CurrencyExternal;
import com.github.bablo_org.bablo_project.api.model.currencyExternal.CurrencyInfo;
import com.google.cloud.firestore.CollectionReference;
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

    @SneakyThrows
    public void add(List<Currency> currencies) {
        WriteBatch batch = firestore.batch();
        CollectionReference collection = firestore.collection(COLLECTION_NAME);

        currencies.forEach(t -> {
            batch.set(collection.document(t.getId()), t.toMap());
        });

        batch.commit().get();
    }

    @SneakyThrows
    public void updateRates(List<CurrencyExternal> currencies) {
        WriteBatch batch = firestore.batch();
        CollectionReference collection = firestore.collection(COLLECTION_NAME);

        currencies.forEach(t -> {
            batch.update(collection.document(t.getId()), t.toMap());
        });

        batch.commit().get();
    }

    @SneakyThrows
    public void updateInfo(List<CurrencyInfo> currencies) {
        WriteBatch batch = firestore.batch();
        CollectionReference collection = firestore.collection(COLLECTION_NAME);

        currencies.forEach(t -> {
            batch.update(collection.document(t.getId()), t.toMap());
        });

        batch.commit().get();
    }

    @SneakyThrows
    public List<Currency> getAll() {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(Currency::ofDoc)
                .collect(toList());
    }

    @SneakyThrows
    public List<Currency> getById(List<String> ids) {
        return firestore.collection(CurrencyService.COLLECTION_NAME)
                .where(inArray(documentId(), ids))
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(Currency::ofDoc)
                .collect(toList());
    }

    public Map<String, Double> getRates(String currency) {
        Map<String, Currency> all = getAll()
                .stream()
                .collect(toMap(Currency::getId, c -> c));

        Currency base = all.get(currency);
        if (base == null) {
            throw new RuntimeException("currency is not found: " + currency);
        }

        Double baseRate = base.getRate();
        return all
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().getRate() / baseRate));
    }

}
