package com.github.bablo_org.bablo_project.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bablo_org.bablo_project.api.model.CurrencyAPI.CurrencyRates;
import com.github.bablo_org.bablo_project.api.model.CurrencyAPI.CurrencyRatesToken;
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
public class CurrencyRatesClient {

    @Value("${api.exchange-rate}")
    private String URL_STRING;

    @Value("${api.exchange-rate.fieldname}")
    private String CONVERSION_RATES;

    private final CurrencyRatesToken token;

    private final ObjectMapper mapper;

    @SneakyThrows
    public List<CurrencyRates> getRates(){

// Making Request
        URL url = new URL(String.format(URL_STRING, token.getToken()));
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

// Convert to JSON
        JsonParser jp = new JsonParser();
        InputStreamReader inputStreamReader = new InputStreamReader((InputStream) request.getContent());
        JsonElement jsonRates = jp.parse(inputStreamReader);
        request.disconnect();
        inputStreamReader.close();
        JsonNode root = mapper.readTree(jsonRates.getAsJsonObject().toString());

        List<CurrencyRates> currenciesList = new ArrayList<CurrencyRates>();
        Date now = new Date();
        root.get(CONVERSION_RATES).fields().forEachRemaining(
          e -> currenciesList.add(new CurrencyRates(e.getKey(), e.getValue().asDouble(), now )));

        return currenciesList;
    }
}
