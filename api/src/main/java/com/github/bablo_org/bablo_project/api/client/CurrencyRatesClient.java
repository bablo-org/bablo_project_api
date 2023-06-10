package com.github.bablo_org.bablo_project.api.client;

import com.github.bablo_org.bablo_project.api.model.dto.CurrencyRatesSnapshot;

public interface CurrencyRatesClient {

    CurrencyRatesSnapshot getActual();
}
