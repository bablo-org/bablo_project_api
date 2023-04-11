package com.github.bablo_org.bablo_project.api.job;

import com.github.bablo_org.bablo_project.api.model.exchangeRate.ExchangeRatesToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class CurrencyApi {

    private static final String URL_STRING = "https://v6.exchangerate-api.com/v6/%s/latest/USD";

    private final ExchangeRatesToken token;

    @SneakyThrows
    public String getRates(){

// Making Request
        URL url = new URL(String.format(URL_STRING, token.getToken()));
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

// Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        return root.getAsJsonObject().toString();
    };
}
