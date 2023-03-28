package com.github.bablo_org.bablo_project.api.service;

import static com.google.cloud.firestore.Filter.equalTo;
import static com.google.cloud.firestore.Filter.or;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.bablo_org.bablo_project.api.model.Transaction;
import com.github.bablo_org.bablo_project.api.model.TransactionStatus;
import com.github.bablo_org.bablo_project.api.utils.StringUtils;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private static final String COLLECTION_NAME = "transactions";

    private final Firestore firestore;

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

    @SneakyThrows
    public List<Transaction> getByUser(String user) {
        return firestore
                .collection(COLLECTION_NAME)
                .where(or(equalTo("sender", user), equalTo("receiver", user)))
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(this::toModel)
                .collect(toList());
    }

    @SneakyThrows
    public Transaction add(Transaction transaction, String user) {
        processNew(transaction, user);

        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME)
                .add(toMap(transaction))
                .get()
                .get()
                .get();

        return toModel(doc);
    }

    @SneakyThrows
    public Transaction approve(String id, String user) {
        DocumentReference ref = getRefById(id);
        Transaction transaction = toModel(ref);
        validateApprove(transaction, user);

        TransactionStatus status = TransactionStatus.APPROVED;
        Date now = new Date();

        transaction.setStatus(status);
        transaction.setUpdated(now);

        ref.update(Map.of(
                "status", status.name(),
                "updated", new Date()
        )).get();

        return transaction;
    }

    @SneakyThrows
    public Transaction decline(String id, String user) {
        DocumentReference ref = getRefById(id);
        Transaction transaction = toModel(ref);
        validateDecline(transaction, user);

        TransactionStatus status = TransactionStatus.DECLINED;
        Date now = new Date();

        transaction.setStatus(status);
        transaction.setUpdated(now);

        ref.update(Map.of(
                "status", status.name(),
                "updated", new Date()
        )).get();

        return transaction;
    }

    @SneakyThrows
    public Transaction complete(String id, String user) {
        DocumentReference ref = getRefById(id);
        Transaction transaction = toModel(ref);
        validateComplete(transaction, user);

        TransactionStatus status = TransactionStatus.COMPLETED;
        Date now = new Date();

        transaction.setStatus(status);
        transaction.setUpdated(now);

        ref.update(Map.of(
                "status", status.name(),
                "updated", new Date()
        )).get();

        return transaction;
    }

    @SneakyThrows
    public void delete(String id) {
        DocumentReference ref = getRefById(id);
        ref.delete().get();
    }

    private void validateApprove(Transaction transaction, String user) {
        if (transaction == null) {
            throw new RuntimeException("can't approve non-existent transaction");
        }

        if (!transaction.getSender().equals(user)) {
            throw new RuntimeException("can't approve transaction - current user is not a sender");
        }

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new RuntimeException("can't approve transaction - status must be PENDING");
        }
    }

    private void validateDecline(Transaction transaction, String user) {
        if (transaction == null) {
            throw new RuntimeException("can't decline non-existent transaction");
        }

        if (!transaction.getSender().equals(user)) {
            throw new RuntimeException("can't decline transaction - current user is not a sender");
        }

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new RuntimeException("can't decline transaction - status must be PENDING");
        }
    }

    private void validateComplete(Transaction transaction, String user) {
        if (transaction == null) {
            throw new RuntimeException("can't complete non-existent transaction");
        }

        if (!transaction.getReceiver().equals(user)) {
            throw new RuntimeException("can't complete transaction - current user is not a receiver");
        }

        if (transaction.getStatus() != TransactionStatus.APPROVED) {
            throw new RuntimeException("can't complete transaction - status must be APPROVED");
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
            throw new RuntimeException("can't add transaction with blank receiver");
        }

        if (StringUtils.isBlank(transaction.getSender())) {
            throw new RuntimeException("can't add transaction with blank sender");
        }

        if (transaction.getSender().equals(transaction.getReceiver())) {
            throw new RuntimeException("can't add transaction with the same sender and receiver");
        }

        if (StringUtils.isBlank(transaction.getCurrency())) {
            throw new RuntimeException("can't add transaction with blank currency");
        }

        if (transaction.getAmount() == null) {
            throw new RuntimeException("can't add transaction with null amount");
        }

        if (transaction.getAmount() < 0.0) {
            throw new RuntimeException(
                    "can't add transaction with negative amount (you may replace sender and receiver instead)");
        }

        if (transaction.getDate() == null) {
            throw new RuntimeException("can't add transaction with null date");
        }

        if (StringUtils.isBlank(transaction.getDescription())) {
            throw new RuntimeException("can't add transaction with blank description");
        }

        if (transaction.getStatus() != null && transaction.getStatus() != TransactionStatus.NEW) {
            throw new RuntimeException("can't add transaction with status != NEW");
        }
    }

    @SneakyThrows
    private Transaction toModel(DocumentReference ref) {
        DocumentSnapshot doc = ref.get().get();
        return doc == null ? null : toModel(doc);
    }

    private Transaction toModel(DocumentSnapshot doc) {
        return new Transaction(
                doc.getId(),
                doc.getString("sender"),
                doc.getString("receiver"),
                doc.getString("currency"),
                doc.getDouble("amount"),
                doc.getString("description"),
                doc.getDate("date"),
                TransactionStatus.valueOf(doc.getString("status")),
                doc.getDate("created"),
                doc.getDate("updated")
        );
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
