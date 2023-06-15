package com.github.bablo_org.bablo_project.api.controller;

import com.github.bablo_org.bablo_project.api.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
public class JobController extends BaseController {

    private final JobService service;

    @GetMapping("/update-currency-rates")
    ResponseEntity<String> updateCurrencyRates() {
        service.updateCurrencyRates();
        return ResponseEntity
                .ok()
                .build();
    }
}
