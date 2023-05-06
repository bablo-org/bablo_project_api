package com.github.bablo_org.bablo_project.api.client;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.currency_api.CurrencyRate;


public interface CurrencyRatesClient {

    List<CurrencyRate> getRates();
}
