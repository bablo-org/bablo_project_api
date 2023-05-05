package com.github.bablo_org.bablo_project.api.service.impl;

import static com.google.cloud.firestore.FieldPath.documentId;
import static com.google.cloud.firestore.Filter.inArray;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.model.CurrencyAPI.CurrencyRates;
import com.github.bablo_org.bablo_project.api.model.CurrencyAPI.CurrencyInfo;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    @Value("${firebase.collection_name.currencies}")
    private String COLLECTION_NAME;

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
    public void updateRates(List<CurrencyRates> currencies) {
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
        return firestore.collection(COLLECTION_NAME)
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
