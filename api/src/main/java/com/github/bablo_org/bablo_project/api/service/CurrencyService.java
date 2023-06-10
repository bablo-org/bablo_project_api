package com.github.bablo_org.bablo_project.api.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.domain.Currency;

public interface CurrencyService {

    List<Currency> getAll();

    List<Currency> getByIds(List<String> ids);

    void updateRates(Map<String, Double> ratesToUsd, Date timestamp);
}
