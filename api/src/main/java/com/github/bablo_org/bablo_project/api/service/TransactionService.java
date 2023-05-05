package com.github.bablo_org.bablo_project.api.service;

import com.github.bablo_org.bablo_project.api.model.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionService {

    public Transaction getById(String id, String user);


    public List<Transaction> getByUser(String user, List<String> statuses);

    public void add(List<Transaction> transactions, String user);


    public void approve(List<String> ids, String user);


    public void decline(List<String> ids, String user);


    public void complete(List<String> ids, String user);


    public void delete(List<String> ids, String userId);

    public Map<String, List<Transaction>> groupByPartner(List<Transaction> transactions, String user);
}
