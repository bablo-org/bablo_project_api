package com.github.bablo_org.bablo_project.api.service;

import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.Transaction;

public interface TransactionService {

    Transaction getById(String id, String user);

    List<Transaction> getByUser(String user, List<String> statuses);

    void add(List<Transaction> transactions, String user);

    void approve(List<String> ids, String user);

    void decline(List<String> ids, String user);

    void complete(List<String> ids, String user);

    Map<String, List<Transaction>> groupByPartner(List<Transaction> transactions, String user);
}
