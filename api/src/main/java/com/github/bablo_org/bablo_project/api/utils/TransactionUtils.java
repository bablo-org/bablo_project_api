package com.github.bablo_org.bablo_project.api.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.bablo_org.bablo_project.api.model.domain.Transaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionUtils {

    public static Map<String, List<Transaction>> groupByPartner(Collection<Transaction> transactions, String currentUser) {
        return transactions
                .stream()
                .collect(Collectors.groupingBy(tx -> tx.getPartner(currentUser)));
    }
}
