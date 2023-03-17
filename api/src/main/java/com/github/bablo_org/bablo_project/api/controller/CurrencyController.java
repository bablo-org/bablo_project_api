package com.github.bablo_org.bablo_project.api.controller;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.repository.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/currencies", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyController {

    private final CurrencyService service;

    @GetMapping
    List<Currency> getAll() {
        return service.getAll();
    }
}
