package com.github.bablo_org.bablo_project.api.controller;

import java.util.List;

import com.github.bablo_org.bablo_project.api.model.Currency;
import com.github.bablo_org.bablo_project.api.service.CurrencyService;
import lombok.RequiredArgsConstructor;
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
    List<Currency> getAll(@RequestParam(value = "isActive", required = false) Boolean isActive) {
        return isActive == null
               ? service.getAll()
               : service.getByStatus(isActive);
    }
}
