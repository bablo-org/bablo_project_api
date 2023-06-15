package com.github.bablo_org.bablo_project.api.service;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.domain.PartnershipRequest;

public interface PartnershipRequestService {

    List<PartnershipRequest> getRelated(String currentUser);

    void create(String currentUser, String partner);

    void accept(String currentUser, String requestId);

    void decline(String currentUser, String requestId);
}
