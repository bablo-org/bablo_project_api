package com.github.bablo_org.bablo_project.api.client.impl;

import com.github.bablo_org.bablo_project.api.client.CurrencyRatesClient;
import com.github.bablo_org.bablo_project.api.model.dto.CurrencyRatesSnapshot;
import com.github.bablo_org.bablo_project.api.model.token.CurrencyRatesApiToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CurrencyRatesClientImpl implements CurrencyRatesClient {

    private final static String BASE_URL = "https://v6.exchangerate-api.com/v6/%s/latest/USD";

    private final RestTemplate template = new RestTemplate();

    private final CurrencyRatesApiToken token;

    @Override
    public CurrencyRatesSnapshot getActual() {
        return template.getForObject(String.format(BASE_URL, token.getToken()), CurrencyRatesSnapshot.class);
    }
}
