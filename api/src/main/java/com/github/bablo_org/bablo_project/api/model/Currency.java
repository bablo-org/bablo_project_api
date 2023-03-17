package com.github.bablo_org.bablo_project.api.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Value;

@Value
public class Currency {
    String id;
    String name;
    Double rate;
    Date updated;
}
