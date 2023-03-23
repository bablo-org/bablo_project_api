package com.github.bablo_org.bablo_project.api.controller;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Transaction;
import com.github.bablo_org.bablo_project.api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    List<Transaction> getAll(@RequestParam(required = false, name = "user") String user) {
        return user == null
                ? service.getAll()
                : service.getByUser(user);
    }

    @PostMapping
    @ResponseBody
    Transaction add(@RequestBody Transaction transaction) {
        return service.add(transaction);
    }
}
