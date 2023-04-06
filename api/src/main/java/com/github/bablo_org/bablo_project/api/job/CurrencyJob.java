package com.github.bablo_org.bablo_project.api.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.model.CurrencyExternal;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.github.bablo_org.bablo_project.api.service.UserService;
import com.google.cloud.firestore.Firestore;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
public class CurrencyJob {

    private final Firestore firestore;

    private final CurrencyService service;

    private final ObjectMapper mapper;

    @SneakyThrows
    public void run(){
        TestImportJson resource = new TestImportJson( );
        String jsonString = resource.getResource("api response.json");

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonElement jsonElement = jsonObject.get("conversion_rates");

        JsonObject jsonObjectRates = jsonElement.getAsJsonObject();

        Set<String> currencies = jsonObjectRates.keySet();
        List<Currency> currenciesList = new ArrayList<Currency>();
        Date now = new Date();
        for (String value : currencies){
            JsonElement rate = jsonObjectRates.get(value);
            Currency currency = new Currency(value, "", "", rate.getAsDouble(), now);
            currenciesList.add(currency);
        }

        service.add(currenciesList);

    }
}
