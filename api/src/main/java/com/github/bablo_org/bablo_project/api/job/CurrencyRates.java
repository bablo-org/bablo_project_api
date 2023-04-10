package com.github.bablo_org.bablo_project.api.job;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bablo_org.bablo_project.api.model.currencyExternal.CurrencyExternal;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class CurrencyRates {

    private static final String CONVERSION_RATES = "conversion_rates";

    private final CurrencyService service;

    private final ObjectMapper mapper;

    private final CurrencyApi api;

    @SneakyThrows
    public void run(){
        JsonNode root = mapper.readTree(api.getRates());
        List<CurrencyExternal> currenciesList = new ArrayList<CurrencyExternal>();
        Date now = new Date();
        root.get(CONVERSION_RATES).fields().forEachRemaining(
                e -> currenciesList.add(new CurrencyExternal(e.getKey(), e.getValue().asDouble(), now )));
        service.updateRates(currenciesList);
    }
}
