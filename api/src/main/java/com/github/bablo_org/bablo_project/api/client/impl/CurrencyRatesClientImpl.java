package com.github.bablo_org.bablo_project.api.client.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bablo_org.bablo_project.api.client.CurrencyRatesClient;
import com.github.bablo_org.bablo_project.api.model.currency_api.CurrencyRate;
import com.github.bablo_org.bablo_project.api.model.currency_api.CurrencyRatesApiToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrencyRatesClientImpl implements CurrencyRatesClient {

    @Value("${api.exchange-rate}")
    private String URL_STRING;

    @Value("${api.exchange-rate.fieldname}")
    private String CONVERSION_RATES;

    private final CurrencyRatesApiToken token;

    private final ObjectMapper mapper;

    @SneakyThrows
    public List<CurrencyRate> getRates() {

// Making Request
        URL url = new URL(String.format(URL_STRING, token.getToken()));
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

// Convert to JSON
        List<CurrencyRate> currenciesList = new ArrayList<>();
        try (InputStreamReader inputStreamReader =
                     new InputStreamReader((InputStream) request.getContent())) {
            JsonParser jp = new JsonParser();
            JsonElement jsonRates = jp.parse(inputStreamReader);
            JsonNode root = mapper.readTree(jsonRates.getAsJsonObject().toString());
            Date now = new Date();
            root.get(CONVERSION_RATES).fields().forEachRemaining(
                    e -> currenciesList.add(new CurrencyRate(e.getKey(), e.getValue().asDouble(), now)));
        }
        request.disconnect();
        return currenciesList;
    }
}
