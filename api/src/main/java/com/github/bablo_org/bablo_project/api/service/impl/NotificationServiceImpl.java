package com.github.bablo_org.bablo_project.api.service.impl;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.bablo_org.bablo_project.api.model.domain.PartnershipRequest;
import com.github.bablo_org.bablo_project.api.model.domain.Transaction;
import com.github.bablo_org.bablo_project.api.model.domain.User;
import com.github.bablo_org.bablo_project.api.service.NotificationService;
import com.github.bablo_org.bablo_project.api.service.TelegramService;
import com.github.bablo_org.bablo_project.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserService userService;

    private final TelegramService telegramService;

    @Override
    public void onTransactionsNew(Collection<Transaction> transactions, String currentUser) {
        sendTransactionsNotifications("Created transactions: ", transactions, currentUser);
    }

    @Override
    public void onTransactionsApproved(Collection<Transaction> transactions, String currentUser) {
        sendTransactionsNotifications("Approved transactions: ", transactions, currentUser);
    }

    @Override
    public void onTransactionsDeclined(Collection<Transaction> transactions, String currentUser) {
        sendTransactionsNotifications("Declined transactions: ", transactions, currentUser);
    }

    @Override
    public void onTransactionsCompleted(Collection<Transaction> transactions, String currentUser) {
        sendTransactionsNotifications("Completed transactions: ", transactions, currentUser);
    }

    @Override
    public void onPartnershipRequestNew(User sender, User receiver) {
        if (isTelegramNotificationsEnabled(receiver)) {
            telegramService.sendMessage(
                    "New partnership request from: " + sender.getName(),
                    receiver.getPrivateData().getTelegramId()
            );
        }
    }

    @Override
    public void onPartnershipRequestAccepted(PartnershipRequest request, String currentUser) {
        Map<String, User> relatedUsers = userService.getByIds(request.getSender(), request.getReceiver());
        User receiver = relatedUsers.get(request.getReceiver());
        User sender = relatedUsers.get(request.getSender());

        if (isTelegramNotificationsEnabled(receiver)) {
            telegramService.sendMessage(
                    "Partnership request was accepted by: " + receiver.getName(),
                    sender.getPrivateData().getTelegramId()
            );
        }
    }

    @Override
    public void onPartnershipRequestDeclined(PartnershipRequest request, String currentUser) {
        Map<String, User> relatedUsers = userService.getByIds(request.getSender(), request.getReceiver());
        User receiver = relatedUsers.get(request.getReceiver());
        User sender = relatedUsers.get(request.getSender());

        if (isTelegramNotificationsEnabled(receiver)) {
            telegramService.sendMessage(
                    "Partnership request was declined by: " + receiver.getName(),
                    sender.getPrivateData().getTelegramId()
            );
        }
    }

    private void sendTransactionsNotifications(String header, Collection<Transaction> transactions, String currentUser) {
        Map<String, User> users = userService.getAll()
                .stream()
                .collect(Collectors.toMap(User::getId, user -> user));
        Map<String, List<Transaction>> byPartner = groupByPartner(transactions, currentUser);

        byPartner.forEach((partnerId, txList) -> {
            User partner = users.get(partnerId);
            if (isTelegramNotificationsEnabled(partner)) {
                String message = txToMessage(header, txList, users);
                telegramService.sendMessage(message, partner.getPrivateData().getTelegramId());
            }
        });
    }

    private Map<String, List<Transaction>> groupByPartner(Collection<Transaction> transactions, String currentUser) {
        return transactions
                .stream()
                .collect(Collectors.groupingBy(tx -> tx.getPartner(currentUser)));
    }

    private String txToMessage(String header, Collection<Transaction> transactions, Map<String, User> users) {
        Transaction first = transactions.iterator().next();
        String sender = users.get(first.getSender()).getName();
        String receiver = users.get(first.getReceiver()).getName();

        String line = "--------------------";
        String delimiter = "\n" + line + "\n";
        String topline = line + "\n";
        String botline = "\n" + line;

        String body = transactions
                .stream()
                .map(tx -> txToMessage(tx, sender, receiver))
                .collect(Collectors.joining(delimiter, topline, botline));

        return header + "\n" + body;
    }

    private String txToMessage(Transaction transaction, String sender, String receiver) {
        return sender + " -> " + receiver +
                " / " + transaction.getAmount() + " " + transaction.getCurrency() +
                " / " + transaction.getDate() +
                "\n" + transaction.getDescription();
    }

    private boolean isTelegramNotificationsEnabled(User user) {
        return isNotBlank(user.getPrivateData().getTelegramId())
                && user.getPrivateData().getSettings().getEnableTelegramNotifications();
    }
}
