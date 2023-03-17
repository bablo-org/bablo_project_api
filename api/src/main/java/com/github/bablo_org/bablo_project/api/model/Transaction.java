package com.github.bablo_org.bablo_project.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Value;

@Value
public class Transaction {
    String id;
    User sender;
    User receiver;
    Currency currency;
    BigDecimal amount;
    String description;
    LocalDate date;
    TransactionStatus status;
    LocalDate created;
    LocalDate updated;
}
