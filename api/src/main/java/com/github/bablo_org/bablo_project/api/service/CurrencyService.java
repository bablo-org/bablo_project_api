package com.github.bablo_org.bablo_project.api.service;

import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.Currency;

public interface CurrencyService {

    List<Currency> getAll();

    List<Currency> getByIds(List<String> ids);

    Map<String, Double> getRates(String currency);
}
