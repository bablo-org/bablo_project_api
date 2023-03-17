package com.github.bablo_org.bablo_project.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Value;

@Value
public class Currency {
    String id;
    String name;
    BigDecimal rate;
    LocalDate updated;
}
