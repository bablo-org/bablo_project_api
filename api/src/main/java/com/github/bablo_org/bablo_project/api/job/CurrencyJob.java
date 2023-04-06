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
            System.out.println("");

            int tmp = 1;
            if (tmp==2){
                break;
            }

        }

        service.add(currenciesList);
        System.out.println("");
//        CurrencyExternal[] currencies = mapper.readValue(jsonString, CurrencyExternal[].class);
//        jsonElement.
//        System.out.println(jsonObject);




//        Date now = new Date();
//        Currency currency = new Currency("ALL", "Albanian lek", "", 103.9181, now);
//
//        service.add(Collections.singletonList(currency));
//        System.out.println("finish");
    }
}
