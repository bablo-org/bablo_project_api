package com.github.bablo_org.bablo_project.api.service;

import java.util.Collection;

import com.github.bablo_org.bablo_project.api.model.domain.PartnershipRequest;
import com.github.bablo_org.bablo_project.api.model.domain.Transaction;
import com.github.bablo_org.bablo_project.api.model.domain.User;

public interface NotificationService {

    void onTransactionsNew(Collection<Transaction> transactions, String currentUser);

    void onTransactionsApproved(Collection<Transaction> transactions, String currentUser);

    void onTransactionsDeclined(Collection<Transaction> transactions, String currentUser);

    void onTransactionsCompleted(Collection<Transaction> transactions, String currentUser);

    void onPartnershipRequestNew(User sender, User receiver);

    void onPartnershipRequestAccepted(PartnershipRequest request, String currentUser);

    void onPartnershipRequestDeclined(PartnershipRequest request, String currentUser);
}
