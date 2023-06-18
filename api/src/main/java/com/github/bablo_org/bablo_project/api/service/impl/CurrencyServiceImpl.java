package com.github.bablo_org.bablo_project.api.service.impl;

import static com.google.cloud.firestore.FieldPath.documentId;
import static com.google.cloud.firestore.Filter.and;
import static com.google.cloud.firestore.Filter.greaterThanOrEqualTo;
import static com.google.cloud.firestore.Filter.inArray;
import static com.google.cloud.firestore.Filter.lessThanOrEqualTo;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.domain.Currency;
import com.github.bablo_org.bablo_project.api.model.dto.CurrencyRatesSnapshot;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private static final String COLLECTION_NAME = "currencies";

    private final Firestore firestore;

    @Override
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

    @Override
    @SneakyThrows
    public List<CurrencyRatesSnapshot> getRatesHistory(LocalDate from, LocalDate to) {
        return firestore.collection("rates")
                .where(
                        and(
                                greaterThanOrEqualTo(documentId(), from.toString()),
                                lessThanOrEqualTo(documentId(), to.toString())
                        )
                )
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(CurrencyRatesSnapshot::ofDoc)
                .collect(toList());
    }

    @Override
    @SneakyThrows
    public List<Currency> getByIds(List<String> ids) {
        return firestore.collection(CurrencyServiceImpl.COLLECTION_NAME)
                .where(inArray(documentId(), ids))
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(Currency::ofDoc)
                .collect(toList());
    }

    @Override
    @SneakyThrows
    public void updateRates(Map<String, Double> rates, Date timestamp) {
        CollectionReference collection = firestore.collection(COLLECTION_NAME);

        WriteBatch batch = firestore.batch();
        rates.forEach((code, rate) -> {
            DocumentReference ref = collection.document(code);
            batch.update(ref, Map.of("rate", rate, "updated", timestamp));
        });

        batch.commit().get();
    }
}
