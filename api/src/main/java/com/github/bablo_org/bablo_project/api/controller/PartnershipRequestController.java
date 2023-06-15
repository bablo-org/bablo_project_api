package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.domain.PartnershipRequest;
import com.github.bablo_org.bablo_project.api.service.PartnershipRequestService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/partnershipRequests", produces = MediaType.APPLICATION_JSON_VALUE)
public class PartnershipRequestController extends BaseController {

    private final PartnershipRequestService service;

    @GetMapping
    @ResponseBody
    List<PartnershipRequest> getRelated(@RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.getRelated(userToken.getUid());
    }

    @PostMapping
    ResponseEntity<String> send(@RequestParam("receiver") String receiver,
                        @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.create(userToken.getUid(), receiver);
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/{id}/accept")
    ResponseEntity<String> accept(@PathVariable("id") String requestId,
                                  @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.accept(userToken.getUid(), requestId);
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/{id}/decline")
    ResponseEntity<String> decline(@PathVariable("id") String requestId,
                                  @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.decline(userToken.getUid(), requestId);
        return ResponseEntity
                .ok()
                .build();
    }
}
