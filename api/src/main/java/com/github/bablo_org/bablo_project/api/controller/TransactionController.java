package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Transaction;
import com.github.bablo_org.bablo_project.api.service.TransactionService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController extends BaseController {

    private final TransactionService service;

    @GetMapping
    @ResponseBody
    List<Transaction> getAll(@RequestParam("status") List<String> statuses, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.getByUser(userToken.getUid(), statuses);
    }

    @GetMapping("/{id}")
    @ResponseBody
    Transaction getById(@PathVariable("id") String id, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.getById(id, userToken.getUid());
    }

    @PostMapping
    @ResponseBody
    ResponseEntity<String> add(@RequestBody List<Transaction> transaction, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.add(transaction, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/approve")
    ResponseEntity<String> approve(@RequestBody List<String> ids, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.approve(ids, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/decline")
    ResponseEntity<String> decline(@RequestBody List<String> ids, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.decline(ids, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }

    @PutMapping("/complete")
    ResponseEntity<String> complete(@RequestBody List<String> ids, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.complete(ids, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping
    ResponseEntity<String> delete(@RequestBody List<String> ids, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        service.delete(ids, userToken.getUid());
        return ResponseEntity
                .ok()
                .build();
    }
}
