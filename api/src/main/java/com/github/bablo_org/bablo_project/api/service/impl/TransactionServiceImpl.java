package com.github.bablo_org.bablo_project.api.service.impl;

import static com.google.cloud.firestore.FieldPath.documentId;
import static com.google.cloud.firestore.Filter.and;
import static com.google.cloud.firestore.Filter.equalTo;
import static com.google.cloud.firestore.Filter.inArray;
import static com.google.cloud.firestore.Filter.or;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.domain.Transaction;
import com.github.bablo_org.bablo_project.api.model.domain.TransactionStatus;
import com.github.bablo_org.bablo_project.api.service.NotificationService;
import com.github.bablo_org.bablo_project.api.service.TransactionService;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Filter;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private static final String COLLECTION_NAME = "transactions";

    private final Firestore firestore;

    private final NotificationService notificationService;

    @Override
    public Transaction getById(String id, String user) {
        Transaction transaction = toModel(getRefById(id));

        if (transaction == null) {
            return null;
        }

        if (!user.equals(transaction.getSender()) && !user.equals(transaction.getReceiver())) {
            throw new RuntimeException("transaction doesn't exist or doesnt' relate to user");
        }

        return transaction;
    }

    @SneakyThrows
    private DocumentReference getRefById(String id) {
        return firestore
                .collection(COLLECTION_NAME)
                .document(id);
    }

    @Override
    @SneakyThrows
    public List<Transaction> getByUser(String user, List<String> statuses) {
        Filter filter = or(equalTo("sender", user), equalTo("receiver", user));
        if (statuses != null) {
            filter = and(filter, inArray("status", statuses));
        }

        return firestore
                .collection(COLLECTION_NAME)
                .where(filter)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(Transaction::ofDoc)
                .collect(toList());
    }

    @Override
    @SneakyThrows
    public void add(List<Transaction> transactions, String user) {
        transactions.forEach(t -> processNew(t, user));

        WriteBatch batch = firestore.batch();
        CollectionReference collection = firestore.collection(COLLECTION_NAME);

        transactions.forEach(t -> {
            batch.set(collection.document(), toMap(t));
        });
        batch.commit().get();

        notificationService.onTransactionsNew(transactions, user);
    }

    @Override
    @SneakyThrows
    public void approve(List<String> ids, String user) {
        List<DocumentSnapshot> documents = getByIds(ids);

        List<Transaction> transactions = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            Transaction transaction = Transaction.ofDoc(document);
            validateApprove(transaction, user);
            transactions.add(transaction);
        }

        Date now = new Date();
        WriteBatch batch = firestore.batch();

        for (DocumentSnapshot document : documents) {
            batch.update(document.getReference(), Map.of(
                    "status", TransactionStatus.APPROVED.name(),
                    "updated", now
            ));
        }
        batch.commit().get();

        notificationService.onTransactionsApproved(transactions, user);
    }

    @Override
    @SneakyThrows
    public void decline(List<String> ids, String user) {
        List<DocumentSnapshot> documents = getByIds(ids);

        List<Transaction> transactions = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            Transaction transaction = Transaction.ofDoc(document);
            validateDecline(transaction, user);
            transactions.add(transaction);
        }

        Date now = new Date();
        WriteBatch batch = firestore.batch();

        for (DocumentSnapshot document : documents) {
            batch.update(document.getReference(), Map.of(
                    "status", TransactionStatus.DECLINED.name(),
                    "updated", now
            ));
        }
        batch.commit().get();

        notificationService.onTransactionsDeclined(transactions, user);
    }

    @Override
    @SneakyThrows
    public void complete(List<String> ids, String user) {
        List<DocumentSnapshot> documents = getByIds(ids);

        List<Transaction> transactions = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            Transaction transaction = Transaction.ofDoc(document);
            validateComplete(transaction, user);
            transactions.add(transaction);
        }

        Date now = new Date();
        WriteBatch batch = firestore.batch();

        for (DocumentSnapshot document : documents) {
            batch.update(document.getReference(), Map.of(
                    "status", TransactionStatus.COMPLETED.name(),
                    "updated", now
            ));
        }
        batch.commit().get();

        notificationService.onTransactionsCompleted(transactions, user);
    }

    @SneakyThrows
    private List<DocumentSnapshot> getByIds(List<String> ids) {
        return firestore.collection(COLLECTION_NAME)
                .where(inArray(documentId(), ids))
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(DocumentSnapshot.class::cast)
                .collect(toList());
    }

    private void validateApprove(Transaction transaction, String user) {
        if (transaction == null) {
            throw new RuntimeException("can't approve non-existent transaction: " + transaction);
        }

        if (!transaction.getSender().equals(user)) {
            throw new RuntimeException("can't approve transaction - current user is not a sender: " + transaction);
        }

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new RuntimeException("can't approve transaction - status must be PENDING: " + transaction);
        }
    }

    private void validateDecline(Transaction transaction, String user) {
        if (transaction == null) {
            throw new RuntimeException("can't decline non-existent transaction: " + transaction);
        }

        if (!transaction.getSender().equals(user)) {
            throw new RuntimeException("can't decline transaction - current user is not a sender: " + transaction);
        }

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new RuntimeException("can't decline transaction - status must be PENDING: " + transaction);
        }
    }

    private void validateComplete(Transaction transaction, String user) {
        if (transaction == null) {
            throw new RuntimeException("can't complete non-existent transaction: " + transaction);
        }

        if (!transaction.getReceiver().equals(user)) {
            throw new RuntimeException("can't complete transaction - current user is not a receiver: " + transaction);
        }

        if (transaction.getStatus() != TransactionStatus.APPROVED) {
            throw new RuntimeException("can't complete transaction - status must be APPROVED: " + transaction);
        }
    }

    private void processNew(Transaction transaction, String user) {
        validateNew(transaction);

        if (user.equals(transaction.getSender())) {
            // no reason to approve my own debt
            transaction.setStatus(TransactionStatus.APPROVED);
        } else if (user.equals(transaction.getReceiver())) {
            transaction.setStatus(TransactionStatus.PENDING);
        }

        Date now = new Date();
        transaction.setCreated(now);
        transaction.setUpdated(now);
    }

    private void validateNew(Transaction transaction) {
        if (StringUtils.isBlank(transaction.getReceiver())) {
            throw new RuntimeException("can't add transaction with blank receiver: " + transaction);
        }

        if (StringUtils.isBlank(transaction.getSender())) {
            throw new RuntimeException("can't add transaction with blank sender: " + transaction);
        }

        if (transaction.getSender().equals(transaction.getReceiver())) {
            throw new RuntimeException("can't add transaction with the same sender and receiver: " + transaction);
        }

        if (StringUtils.isBlank(transaction.getCurrency())) {
            throw new RuntimeException("can't add transaction with blank currency: " + transaction);
        }

        if (transaction.getAmount() == null) {
            throw new RuntimeException("can't add transaction with null amount: " + transaction);
        }

        if (transaction.getAmount() < 0.0) {
            throw new RuntimeException(
                    "can't add transaction with negative amount (you may replace sender and receiver instead): " + transaction);
        }

        if (transaction.getDate() == null) {
            throw new RuntimeException("can't add transaction with null date: " + transaction);
        }

        if (StringUtils.isBlank(transaction.getDescription())) {
            throw new RuntimeException("can't add transaction with blank description: " + transaction);
        }

        if (transaction.getStatus() != null && transaction.getStatus() != TransactionStatus.NEW) {
            throw new RuntimeException("can't add transaction with status != NEW: " + transaction);
        }
    }

    @SneakyThrows
    private Transaction toModel(DocumentReference ref) {
        DocumentSnapshot doc = ref.get().get();
        return doc == null ? null : Transaction.ofDoc(doc);
    }

    private Map<String, Object> toMap(Transaction transaction) {
        Map<String, Object> map = new HashMap<>();

        ofNullable(transaction.getId()).ifPresent(v -> map.put("id", v));
        ofNullable(transaction.getSender()).ifPresent(v -> map.put("sender", v));
        ofNullable(transaction.getReceiver()).ifPresent(v -> map.put("receiver", v));
        ofNullable(transaction.getCurrency()).ifPresent(v -> map.put("currency", v));
        ofNullable(transaction.getAmount()).ifPresent(v -> map.put("amount", v));
        ofNullable(transaction.getDescription()).ifPresent(v -> map.put("description", v));
        ofNullable(transaction.getDate()).ifPresent(v -> map.put("date", v));
        ofNullable(transaction.getStatus()).ifPresent(v -> map.put("status", v.name()));
        ofNullable(transaction.getCreated()).ifPresent(v -> map.put("created", v));
        ofNullable(transaction.getUpdated()).ifPresent(v -> map.put("updated", v));

        return map;
    }
}
