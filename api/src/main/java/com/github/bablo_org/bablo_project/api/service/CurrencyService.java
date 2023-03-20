package com.github.bablo_org.bablo_project.api.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final Firestore firestore;

    @SneakyThrows
    public List<Currency> getAll() {
        return firestore.collection("currencies")
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(doc -> new Currency(
                        doc.getId(),
                        doc.getString("name"),
                        doc.getDouble("rate"),
                        doc.getDate("updated")
                ))
                .collect(toList());
    }
}
