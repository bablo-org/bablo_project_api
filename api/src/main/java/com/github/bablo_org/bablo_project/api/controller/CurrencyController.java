package com.github.bablo_org.bablo_project.api.controller;

import java.time.LocalDate;
import java.util.List;

import com.github.bablo_org.bablo_project.api.model.domain.Currency;
import com.github.bablo_org.bablo_project.api.model.dto.CurrencyRatesSnapshot;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/currencies", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyController extends BaseController {

    private final CurrencyService service;

    @GetMapping
    @ResponseBody
    List<Currency> getAll() {
        return service.getAll();
    }

    @GetMapping("/ratesHistory")
    @ResponseBody
    List<CurrencyRatesSnapshot> getRatesHistory(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return service.getRatesHistory(from, to);
    }
}
