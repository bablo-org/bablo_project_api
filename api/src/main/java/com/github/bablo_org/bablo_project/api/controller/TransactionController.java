package com.github.bablo_org.bablo_project.api.controller;

import static com.github.bablo_org.bablo_project.api.Constants.ORIGIN_URL;
import static com.github.bablo_org.bablo_project.api.Constants.USER_TOKEN;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Transaction;
import com.github.bablo_org.bablo_project.api.service.TransactionService;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = ORIGIN_URL)
@RequestMapping(path = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController extends BaseController {

    private final TransactionService service;

    @GetMapping
    @ResponseBody
    List<Transaction> getByUser(@RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.getByUser(userToken.getUid());
    }

    @PostMapping
    @ResponseBody
    Transaction post(@RequestBody Transaction transaction, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return service.add(transaction);
    }

    @PostMapping("")
    Transaction put(@RequestBody Transaction transaction, @RequestAttribute(USER_TOKEN) FirebaseToken userToken) {
        return null;
    }
}
