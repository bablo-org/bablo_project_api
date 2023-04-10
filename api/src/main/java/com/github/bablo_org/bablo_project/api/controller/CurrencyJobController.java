package com.github.bablo_org.bablo_project.api.controller;

import com.github.bablo_org.bablo_project.api.job.CurrencyRates;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/currency-job", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyJobController extends BaseController{
    private final CurrencyRates currencyRates;

    @GetMapping
    void run(){
        currencyRates.run( );
    }
}
