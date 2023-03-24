package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Transaction;
import com.github.bablo_org.bablo_project.api.service.TransactionService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController extends BaseController {

    private final TransactionService service;

    @GetMapping
    @ResponseBody
    List<Transaction> getByUser(@RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.getByUser(userToken.getUid());
    }

    @GetMapping("/{id}")
    @ResponseBody
    Transaction getById(@PathVariable("id") String id, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.getById(id, userToken.getUid());
    }

    @PostMapping
    @ResponseBody
    Transaction add(@RequestBody Transaction transaction, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.add(transaction, userToken.getUid());
    }

    @PutMapping("/{id}/approve")
    Transaction approve(@PathVariable("id") String id, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.approve(id, userToken.getUid());
    }

    @PutMapping("/{id}/decline")
    Transaction decline(@PathVariable("id") String id, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.decline(id, userToken.getUid());
    }

    @PutMapping("/{id}/complete")
    Transaction complete(@PathVariable("id") String id, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.complete(id, userToken.getUid());
    }

    @DeleteMapping("/{id}")
    String delete(@PathVariable("id") String id) {
        service.delete(id);
        return "ok";
    }
}
