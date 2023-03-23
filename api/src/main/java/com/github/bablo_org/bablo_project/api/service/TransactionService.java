package com.github.bablo_org.bablo_project.api.service;

import static com.google.cloud.firestore.Filter.equalTo;
import static com.google.cloud.firestore.Filter.or;
import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Transaction;
import com.github.bablo_org.bablo_project.api.model.TransactionStatus;
import com.github.bablo_org.bablo_project.api.utils.StringUtils;
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
    public Transaction getById(String id) {
        DocumentSnapshot doc = firestore
                .collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get();

        return doc.exists()
                ? toModel(doc)
                : null;
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
    public Transaction add(Transaction transaction) {
        processNew(transaction);

        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME)
                .add(transaction)
                .get()
                .get()
                .get();

        return toModel(doc);
    }

    @SneakyThrows
    public Transaction update(Transaction updated) {
        Transaction current = getById(updated.getId());

        processUpdate(current, updated);

       return null;


    }

    private void processNew(Transaction transaction) {
        validateNew(transaction);

        transaction.setStatus(TransactionStatus.NEW);
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

    private void processUpdate(Transaction current, Transaction updated) {
        validateUpdate(current, updated);
    }

    private void validateUpdate(Transaction current, Transaction updated) {
        if (current == null) {
            throw new RuntimeException("can't update non-existent: " + updated.getId());
        }


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
