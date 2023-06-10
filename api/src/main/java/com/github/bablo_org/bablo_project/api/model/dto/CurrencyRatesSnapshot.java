package com.github.bablo_org.bablo_project.api.model.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyRatesSnapshot {

    @JsonProperty("conversion_rates")
    private Map<String, Double> rates;
}
