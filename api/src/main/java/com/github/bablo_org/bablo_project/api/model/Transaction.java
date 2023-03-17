package com.github.bablo_org.bablo_project.api.model;

import java.util.Date;

import lombok.Value;

@Value
public class Transaction {
    String id;
    User sender;
    User receiver;
    Currency currency;
    Double amount;
    String description;
    Date date;
    TransactionStatus status;
    Date created;
    Date updated;
}
