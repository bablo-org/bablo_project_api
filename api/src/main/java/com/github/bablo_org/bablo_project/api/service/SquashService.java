package com.github.bablo_org.bablo_project.api.service;

import static com.github.bablo_org.bablo_project.api.model.TransactionStatus.APPROVED;
import static com.github.bablo_org.bablo_project.api.model.TransactionStatus.PENDING;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.SquashData;
import com.github.bablo_org.bablo_project.api.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SquashService {

    private final TransactionService transactionService;

    private final CurrencyService currencyService;

    public List<SquashData> generate(String user, String currency) {
        List<Transaction> transactions = transactionService.getByUser(user, List.of(PENDING.name(), APPROVED.name()));

        Map<String, List<Transaction>> groupByPartner = transactionService.groupByPartner(transactions, user);
        Map<String, Double> rates = currencyService.getRates(currency);

        return groupByPartner.entrySet()
                .stream()
                .map(e -> generateForPartner(user, e.getKey(), e.getValue(), currency, rates))
                .collect(toList());
    }

    private SquashData generateForPartner(String user, String partner, List<Transaction> transactions,
                                          String currency, Map<String, Double> rates) {
        double balance = 0.0;
        for (Transaction transaction : transactions) {
            String txCurrency = transaction.getCurrency();
            double txAmount = transaction.getAmount();
            double rate = rates.get(txCurrency);

            double amount = txAmount / rate;
            balance += user.equals(transaction.getReceiver()) ? amount : -amount;
        }

        SquashData squash = new SquashData();
        squash.setInitiator(user);
        squash.setPartner(partner);
        squash.setTransactions(transactions.stream().map(Transaction::getId).collect(toList()));
        squash.setBalance(balance);
        squash.setCurrency(currency);

        return squash;
    }
}
