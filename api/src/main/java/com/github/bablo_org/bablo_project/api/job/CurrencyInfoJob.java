package com.github.bablo_org.bablo_project.api.job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bablo_org.bablo_project.api.model.exchangeRate.CurrencyInfo;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Component
public class CurrencyInfoJob {

    private static final String CURRENCY_NAME_FILE = "currency-names.json";

    private final CurrencyService service;

    private final ObjectMapper mapper;

    @SneakyThrows
    public void run() {
        Resource resource = new Resource();
        String jsonStringNames = resource.getResource(CURRENCY_NAME_FILE);

        JsonNode root = mapper.readTree(jsonStringNames);
        List<CurrencyInfo> currenciesList = new ArrayList<CurrencyInfo>();

        root.fields().forEachRemaining(
                e -> currenciesList.add(new CurrencyInfo(e.getKey(),e.getValue().asText())));
        service.updateInfo(currenciesList);
    }

}
