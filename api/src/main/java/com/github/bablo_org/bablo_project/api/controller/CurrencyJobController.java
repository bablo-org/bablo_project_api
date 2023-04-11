package com.github.bablo_org.bablo_project.api.controller;

import com.github.bablo_org.bablo_project.api.job.CurrencyInfoJob;
import com.github.bablo_org.bablo_project.api.job.CurrencyRatesJob;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/currency-job", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyJobController extends BaseController{
    private final CurrencyRatesJob currencyRatesJob;

    private final CurrencyInfoJob currencyInfoJob;

    @PutMapping("/rate")
    void rate(){
        currencyRatesJob.run( );
    }

    @PutMapping("/info")
    void info(){
        currencyInfoJob.run( );
    }

}

