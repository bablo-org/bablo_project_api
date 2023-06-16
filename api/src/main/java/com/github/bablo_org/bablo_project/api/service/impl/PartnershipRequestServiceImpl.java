package com.github.bablo_org.bablo_project.api.service.impl;

import static com.google.cloud.firestore.Filter.equalTo;
import static com.google.cloud.firestore.Filter.or;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.bablo_org.bablo_project.api.model.domain.PartnershipRequest;
import com.github.bablo_org.bablo_project.api.model.domain.User;
import com.github.bablo_org.bablo_project.api.service.NotificationService;
import com.github.bablo_org.bablo_project.api.service.PartnershipRequestService;
import com.github.bablo_org.bablo_project.api.service.UserService;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnershipRequestServiceImpl implements PartnershipRequestService {

    private static final String COLLECTION_NAME = "partnership-requests";

    private final Firestore firestore;

    private final UserService userService;

    private final NotificationService notificationService;

    @Override
    @SneakyThrows
    public List<PartnershipRequest> getRelated(String currentUser) {
        return firestore.collection(COLLECTION_NAME)
                .where(or(equalTo("sender", currentUser), equalTo("receiver", currentUser)))
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(PartnershipRequest::ofDoc)
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public void create(String currentUser, String partnerId) {
        if (Objects.equals(currentUser, partnerId)) {
            throw new RuntimeException("user can't send request to himself");
        }

        List<PartnershipRequest> relatedWithPartner = getRelated(currentUser)
                .stream()
                .filter(req -> Objects.equals(partnerId, req.getSender()) || Objects.equals(partnerId, req.getReceiver()))
                .collect(Collectors.toList());

        if (!relatedWithPartner.isEmpty()) {
            throw new RuntimeException("there are similar requests already exist: " + relatedWithPartner);
        }

        Map<String, User> users = userService.getByIds(currentUser, partnerId);
        User sender = requireNonNull(users.get(currentUser), "user must exists: " + currentUser);
        User receiver = requireNonNull(users.get(partnerId), "user must exists: " + partnerId);

        if (sender.getNetwork().isPartner(partnerId)) {
            throw new RuntimeException("user is your partner already");
        }

        firestore.collection(COLLECTION_NAME)
                .document()
                .set(Map.of("sender", currentUser, "receiver", partnerId))
                .get();
        notificationService.onPartnershipRequestNew(sender, receiver);
        log.info("partnership request has been created: " + currentUser + " -> " + partnerId);
    }

    @Override
    @SneakyThrows
    public void accept(String currentUser, String requestId) {
        PartnershipRequest request = getById(requestId)
                .orElseThrow(() -> new RuntimeException("request doesn't exist"));

        if (!Objects.equals(currentUser, request.getReceiver())) {
            throw new RuntimeException("current user is not a receiver of this request");
        }

        userService.addPartner(currentUser, request.getSender());
        firestore.collection(COLLECTION_NAME)
                .document(requestId)
                .delete()
                .get();

        notificationService.onPartnershipRequestAccepted(request, currentUser);
        log.info("partnership request has been accepted: " + requestId);
    }

    @Override
    @SneakyThrows
    public void decline(String currentUser, String requestId) {
        PartnershipRequest request = getById(requestId)
                .orElseThrow(() -> new RuntimeException("request doesn't exist"));

        if (!Objects.equals(currentUser, request.getReceiver())) {
            throw new RuntimeException("current user is not a receiver of this request");
        }

        firestore.collection(COLLECTION_NAME)
                .document(requestId)
                .delete()
                .get();

        notificationService.onPartnershipRequestDeclined(request, currentUser);
        log.info("partnership request has been declined: " + requestId);
    }

    @SneakyThrows
    private Optional<PartnershipRequest> getById(String id) {
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get();

        return Optional.of(doc)
                .filter(DocumentSnapshot::exists)
                .map(PartnershipRequest::ofDoc);
    }
}
