package com.github.bablo_org.bablo_project.api.service;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.model.CurrencyAPI.CurrencyInfo;
import com.github.bablo_org.bablo_project.api.model.CurrencyAPI.CurrencyRates;

import java.util.List;
import java.util.Map;

public interface CurrencyService {
    public void add(List<Currency> currencies);


    public void updateRates(List<CurrencyRates> currencies);


    public void updateInfo(List<CurrencyInfo> currencies);


    public List<Currency> getAll();


    public List<Currency> getById(List<String> ids);


    public Map<String, Double> getRates(String currency);
}
