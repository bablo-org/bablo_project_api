package com.github.bablo_org.bablo_project.api.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private String id;
    private String sender;
    private String receiver;
    private String currency;
    private Double amount;
    private String description;
    private Date date;
    private TransactionStatus status;
    private Date created;
    private Date updated;
}
