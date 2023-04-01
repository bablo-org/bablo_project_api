package com.github.bablo_org.bablo_project.api.model;

import static java.util.stream.Collectors.toList;

import java.util.List;

public enum TransactionStatus {
    NEW,
    PENDING,
    APPROVED,
    DECLINED,
    COMPLETED;

    public static List<TransactionStatus> ofList(List<String> names) {
        if (names == null) {
            return null;
        }

        return names
                .stream()
                .map(TransactionStatus::valueOf)
                .collect(toList());
    }
}
