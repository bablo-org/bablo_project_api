package com.github.bablo_org.bablo_project.api.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.domain.Currency;
import com.github.bablo_org.bablo_project.api.model.dto.CurrencyRatesSnapshot;

public interface CurrencyService {

    List<Currency> getAll();

    List<CurrencyRatesSnapshot> getRatesHistory(LocalDate from, LocalDate to);

    List<Currency> getByIds(List<String> ids);

    void updateRates(Map<String, Double> ratesToUsd, Date timestamp);
}
