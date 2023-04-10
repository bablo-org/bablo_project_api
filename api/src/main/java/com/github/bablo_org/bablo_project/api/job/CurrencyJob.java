package com.github.bablo_org.bablo_project.api.job;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.model.currencyExternal.CurrencyExternal;
import com.github.bablo_org.bablo_project.api.model.currencyExternal.CurrencyExternalApi;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.google.cloud.firestore.Firestore;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class CurrencyJob {

    private static final String CONVERSION_RATES = "conversion_rates";

    private final CurrencyService service;

    private final ObjectMapper mapper;

    private final CurrencyApi api;

    @SneakyThrows
    public void run(){
        TestImportJson resource = new TestImportJson( );

        String jsonString = resource.getResource("api response.json");
        String jsonStringNames = resource.getResource("currency names.json");
        String jsonStringSymbol = resource.getResource("currency symbol.json");

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonObject jsonObjectNames = JsonParser.parseString(jsonStringNames).getAsJsonObject();
        JsonObject jsonObjectSymbol = JsonParser.parseString(jsonStringSymbol).getAsJsonObject();

        JsonElement jsonElement = jsonObject.get("conversion_rates");

        JsonObject jsonObjectRates = jsonElement.getAsJsonObject();

        Set<String> currencies = jsonObjectRates.keySet();
        List<CurrencyExternal> currenciesList = new ArrayList<CurrencyExternal>();
        Date now = new Date();
        for (String value : currencies){
            JsonElement rate = jsonObjectRates.get(value);
            JsonElement name = jsonObjectNames.get(value);
            JsonElement symbol = jsonObjectSymbol.get(value);
            if(!value.equals("USD")){
                continue;
            }
            if(name==null){
                continue;
            }
            if(symbol==null){
                continue;
            }
            CurrencyExternal currency =
                    new CurrencyExternal(value, rate.getAsDouble(), now );
            currenciesList.add(currency);
        }

        //service.add(currenciesList);
        service.updateRates(currenciesList);
    }

    @SneakyThrows
    public void run2(){
        TestImportJson resource = new TestImportJson( );

        String jsonString = resource.getResource("api response.json");

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JsonNode root = mapper.readTree(jsonString);
        root.get("conversion_rates").fields().forEachRemaining(
                e -> System.out.println(e.getKey() + " : " + e.getValue().asText()));
                System.out.println("hello");
    }
    @SneakyThrows
    public void run3(){
        JsonNode root = mapper.readTree(api.getRates());
        List<CurrencyExternal> currenciesList = new ArrayList<CurrencyExternal>();
        Date now = new Date();
        root.get(CONVERSION_RATES).fields().forEachRemaining(
                e -> currenciesList.add(new CurrencyExternal(e.getKey(), e.getValue().asDouble(), now )));
        service.updateRates(currenciesList);
    }
}
