package com.github.bablo_org.bablo_project.api.model;

import java.util.Date;

import lombok.Value;

@Value
public class Currency {
    String id;
    String name;
    String symbol;
    boolean isActive;
    Double rate;
    Date updated;
}
