package com.github.bablo_org.bablo_project.api.service;

import com.github.bablo_org.bablo_project.api.model.Currency;
import java.util.List;
import java.util.Map;

public interface CurrencyService {
    void add(List<Currency> currencies);


    void updateRates();


    void updateInfo();


    List<Currency> getAll();


    List<Currency> getById(List<String> ids);


    Map<String, Double> getRates(String currency);
}
