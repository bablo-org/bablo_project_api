package com.github.bablo_org.bablo_project.api.job;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import com.github.bablo_org.bablo_project.api.service.UserService;
import com.google.cloud.firestore.Firestore;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
public class CurrencyJob {

    private final Firestore firestore;

    private final CurrencyService service;

    public static void main(String[] args) {
//        new CurrencyJob().run();
    }
    public void run(){
        TestImportJson resource = new TestImportJson( );
        String jsonString = resource.getResource("api response.json");

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        System.out.println(jsonObject);

        Date now = new Date();
        Currency currency = new Currency("ALL", "Albanian lek", "", 103.9181, now);

        service.add(Collections.singletonList(currency));
        System.out.println("finish");
    }
}
