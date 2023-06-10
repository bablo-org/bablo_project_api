package com.github.bablo_org.bablo_project.api.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.client.CurrencyRatesClient;
import com.github.bablo_org.bablo_project.api.model.dto.CurrencyRatesSnapshot;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.github.bablo_org.bablo_project.api.service.JobService;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final CurrencyRatesClient client;

    private final CurrencyService currencyService;

    private final Firestore firestore;

    @Override
    @SneakyThrows
    public void updateCurrencyRates() {
        Date now = new Date();
        CurrencyRatesSnapshot rates = client.getActual();

        updateRatesHistory(rates.getRates());
        log.info("rates history has been successfully updated");

        currencyService.updateRates(rates.getRates(), now);
        log.info("currency rates has been successfully updated");
    }

    @SneakyThrows
    private void updateRatesHistory(Map<String, Double> rates) {
        firestore.collection("rates")
                .document(LocalDate.now().toString())
                .set(rates)
                .get();
    }
}
