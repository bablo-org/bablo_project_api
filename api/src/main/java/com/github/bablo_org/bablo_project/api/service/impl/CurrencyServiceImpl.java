package com.github.bablo_org.bablo_project.api.service.impl;

import static com.google.cloud.firestore.FieldPath.documentId;
import static com.google.cloud.firestore.Filter.inArray;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.google.cloud.firestore.Firestore;
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
