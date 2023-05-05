package com.github.bablo_org.bablo_project.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bablo_org.bablo_project.api.model.CurrencyAPI.CurrencyInfo;
import com.github.bablo_org.bablo_project.api.utils.Resource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CurrencyInfoClient {
    @Value("${filename.currency-names}")
    private String CURRENCY_NAME_FILE;

    @Value("${filename.currency-symbols}")
    private String CURRENCY_SYMBOLS_FILE;

    private final ObjectMapper mapper;

    @SneakyThrows
    public List<CurrencyInfo> getInfo() {
        Resource resource = new Resource();
        String jsonStringNames = resource.getResource(CURRENCY_NAME_FILE);
        String jsonStringSymbols = resource.getResource(CURRENCY_SYMBOLS_FILE);

        JsonNode rootNames = mapper.readTree(jsonStringNames);
        JsonNode rootSymbols = mapper.readTree(jsonStringSymbols);

        List<CurrencyInfo> currenciesList = new ArrayList<CurrencyInfo>();

        rootNames.fields().forEachRemaining(
                e -> currenciesList.add(new CurrencyInfo(e.getKey(),
                        e.getValue().asText(),
                        rootSymbols.get(e.getKey()).textValue())));
        return currenciesList;
    }

}
