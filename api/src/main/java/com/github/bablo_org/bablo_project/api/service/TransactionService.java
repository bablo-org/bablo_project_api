package com.github.bablo_org.bablo_project.api.service;

import static com.google.cloud.firestore.Filter.equalTo;
import static com.google.cloud.firestore.Filter.or;
import static java.util.stream.Collectors.toList;

import java.util.Date;
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

    @SneakyThrows
    public DocumentReference getById(String id) {
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
                .add(transaction)
                .get()
                .get()
                .get();

        return toModel(doc);
    }

    @SneakyThrows
    public Transaction approve(String id, String user) {
        DocumentReference ref = getById(id);
        Transaction transaction = toModel(ref);
        validateApprove(transaction, user);

        TransactionStatus status = TransactionStatus.APPROVED;
        Date now = new Date();

        transaction.setStatus(status);
        transaction.setUpdated(now);

        ref.update(Map.of(
                "status", TransactionStatus.APPROVED.name(),
                "updated", new Date()
        )).get();

        return transaction;
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

    private void processNew(Transaction transaction, String user) {
        validateNew(transaction);

        if (user.equals(transaction.getSender())) {
            // no reason to approve my own debt
            transaction.setStatus(TransactionStatus.APPROVED);
        } else if (user.equals(transaction.getReceiver())) {
            transaction.setStatus(TransactionStatus.PENDING);
        }
        transaction.setCreated(new Date());
        transaction.setUpdated(new Date());
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
}
