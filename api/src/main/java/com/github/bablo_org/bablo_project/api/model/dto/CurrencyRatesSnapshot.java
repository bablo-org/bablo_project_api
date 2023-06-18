package com.github.bablo_org.bablo_project.api.model.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.DocumentSnapshot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRatesSnapshot {

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    private Map<String, Double> rates;

    public static CurrencyRatesSnapshot ofDoc(DocumentSnapshot doc) {
        LocalDate date = LocalDate.parse(doc.getId(), DateTimeFormatter.ISO_LOCAL_DATE);
        Map<String, Double> rates = doc.getData()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> (Double) e.getValue()));
        return new CurrencyRatesSnapshot(date, rates);
    }

    @JsonProperty("conversion_rates")
    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    @JsonProperty("rates")
    public Map<String, Double> getRates() {
        return rates;
    }
}
