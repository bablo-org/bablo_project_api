package com.github.bablo_org.bablo_project.api.model;

import java.util.List;

import lombok.Data;

@Data
public class SquashData {
    private String initiator;
    private String partner;
    private List<String> transactions;
    private Double balance;
    private String currency;
}
